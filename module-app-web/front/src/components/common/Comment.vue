<template>
  <div class="d-flex justify-content-center">
    <span class="sub-item-title">댓글</span>
  </div>
  <div class="mt-3">
    <textarea class="form-control comment" name="content" id="content" v-model="content"></textarea>
    <p id="contentErrorMessage" class="error-message"></p>
    <div class="d-flex justify-content-end mt-3 me-2">
      <button @click="writeComment()" class="btn btn-sm btn-outline-main-blue d-flex align-items-center">작성<img :src="require(`@/assets/images/write-main-blue.svg`)" class="ms-2"></button>
    </div>
  </div>
  <div class="my-4">
    <div v-for="(comment, i) in commentList" :key="i">
      <div class="mt-4">
        <strong class="additional-information-title">작성자: </strong><span class="additional-information-content">{{ comment.createdByUser.department }} {{ comment.createdByUser.name }}, </span>
        <strong class="additional-information-title">작성일: </strong><span class="additional-information-content">{{ comment.createdDate }}</span>
        <img v-if="comment.newIcon" :src="require(`@/assets/images/new_post.svg`)" class="new-icon"/>
      </div>
      <div class="mt-2">
        <span :id="'commentContent' + i" class="comment">
          {{ replaceBrToNewline(comment.content) }}
        </span>
        <textarea :name="'updateCommentContent' + i" :id="'updateCommentContent' + i" class="form-control comment" :style="{display: 'none'}"></textarea>
        <p :id="'updateCommentContent' + i + 'ErrorMessage'" class="error-message"></p>
      </div>
      <div v-if="comment.access" class="mt-3 d-flex">
        <button @click="displayComment(i, comment.content)" :id="'displayCommentButton' + i" class="btn btn-sm btn-outline-main-blue d-flex align-items-center me-2">수정<img :src="require(`@/assets/images/update-main-blue.svg`)" class="ms-2">
        </button>
        <button @click="deleteComment(comment.idx)" :id="'deleteCommentButton' + i" class="btn btn-sm btn-outline-main-red d-flex align-items-center">삭제<img :src="require(`@/assets/images/delete-main-red.svg`)" class="ms-2"></button>
        <button @click="updateComment(comment.idx, comment.createdByIdx, comment.activeStatus, i)" :id="'updateCommentButton' + i" class="btn btn-sm btn-outline-main-blue d-flex align-items-center me-2"
                :style="{display: 'none !important'}">수정 완료
          <img :src="require(`@/assets/images/update-main-blue.svg`)" class="ms-2"></button>
        <button @click="cancelComment(i)" class="btn btn-sm btn-outline-main-red d-flex align-items-center" :id="'cancelCommentButton' + i" :style="{display: 'none !important'}">취소<img :src="require(`@/assets/images/x-main-red.svg`)"
                                                                                                                                                                                         class="ms-2">
        </button>
      </div>
    </div>
  </div>
</template>

<style lang="scss">

</style>

<script>
// vue.js
import {ref} from 'vue'
import {useRouter} from 'vue-router'
import axios from "axios";
// sweetalert2
import {confirm} from '@/assets/plugins/sweetalert2/sweetalert2'
// utils
import {parseApiErrorMsg, validateSizeAndIsEmptyByEditor} from "@/utils/validation-util";
import {replaceBrToNewline} from "@/utils/converter-util";

export default {
  name: "Comment",
  props: {
    path: String,
    idxName: String,
    idx: Number,
    commentList: Object
  },

  setup(props) {
    // vue.js
    const router = useRouter();
    // variable
    const content = ref("");

    const writeComment = async () => {
      let isExit;

      if (!(validateSizeAndIsEmptyByEditor("content", content.value))) {
        return false;
      }

      isExit = await axios.post(process.env.VUE_APP_MODULE_APP_API_URL + "/api/" + props.path,
          {
            [props.idxName]: props.idx,
            content: content.value,
            activeStatus: "ACTIVE"
          },
      )
          .then((response) => {
            isExit = false;
          })
          .catch((error) => {
            parseApiErrorMsg(error.response.data);
            isExit = true;
          })
          .then(() => {
            return isExit;
          });

      if (isExit) return false;
      router.go(0);
    }

    const displayComment = (index, content) => {
      document.getElementById("updateCommentContent" + index).style.display = "inline";
      document.getElementById("updateCommentContent" + index).value = content.replace(/<br>/g, "\n");
      document.getElementById("commentContent" + index).style.display = "none";

      document.getElementById("displayCommentButton" + index).setAttribute("style", "display: none !important");
      document.getElementById("deleteCommentButton" + index).setAttribute("style", "display: none !important");
      document.getElementById("updateCommentButton" + index).style.display = "inline";
      document.getElementById("cancelCommentButton" + index).style.display = "inline";
    }

    const deleteComment = async (idx) => {
      confirm.fire({
        title: "삭제하시겠습니까?",
        text: "댓글이 삭제됩니다.",
        confirmButtonText: '네',
        cancelButtonText: '아니오'
      }).then((result) => {
        if (result.isConfirmed) {
          axios.delete(process.env.VUE_APP_MODULE_APP_API_URL + "/api/" + props.path + "/" + idx)
              .then((response) => {
                router.go(0);
              })
              .catch((error) => {
                parseApiErrorMsg(error.response);
              })
              .then(() => {
              });
        } else {
          return false;
        }
      })
    }

    const updateComment = async (idx, createdByIdx, activeStatus, index) => {
      let isExit;
      let updateCommentContent = document.getElementsByName("updateCommentContent" + index)[0].value;

      if (!(validateSizeAndIsEmptyByEditor("updateCommentContent" + index, updateCommentContent))) {
        return false;
      }

      isExit = await axios.put(process.env.VUE_APP_MODULE_APP_API_URL + "/api/" + props.path + "/" + idx,
          {
            idx: idx,
            createdByIdx: createdByIdx,
            activeStatus: activeStatus,
            [props.idxName]: props.idx,
            content: updateCommentContent
          },
      )
          .then((response) => {
            isExit = false;
          })
          .catch((error) => {
            parseApiErrorMsg(error.response.data);
            isExit = true;
          })
          .then(() => {
            return isExit;
          });

      if (isExit) return false;
      router.go(0);
    }

    const cancelComment = (index) => {
      document.getElementById("updateCommentContent" + index).style.display = "none";
      document.getElementById("commentContent" + index).style.display = "inline";

      document.getElementById("displayCommentButton" + index).style.display = "inline";
      document.getElementById("deleteCommentButton" + index).style.display = "inline";
      document.getElementById("updateCommentButton" + index).setAttribute("style", "display: none !important");
      document.getElementById("cancelCommentButton" + index).setAttribute("style", "display: none !important");
    }

    return {
      // variable
      content,

      // function
      writeComment, displayComment, deleteComment, updateComment, cancelComment,
      replaceBrToNewline
    }
  }
}
</script>

