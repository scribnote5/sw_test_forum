<template>
  <div class="float-end">
    <span v-show="likeDto.like === true">
      <img :src="require(`@/assets/images/red-heart.svg`)" class="cursor-pointer" @click="cancelLike()">
    </span>
    <span v-show="likeDto.like === false">
      <img :src="require(`@/assets/images/empty-heart.svg`)" class="cursor-pointer" @click="like()">
    </span>
    <i class="ms-1"> {{ likeDto.likeCount }}</i>
  </div>
</template>

<script>
import axios from "axios";
import {parseApiErrorMsg} from "@/utils/validation-util";

export default {
  name: "Like",
  props: {
    likeDto: Object
  }, setup(props) {
    /* 좋아요 */
    const like = () => {
      axios.post(process.env.VUE_APP_MODULE_APP_API_URL + props.likeDto.link + props.likeDto.guidelineIdx,
          {},
      )
          .then((response) => {
            props.likeDto.like = true;
            props.likeDto.likeCount++;
            props.likeDto.idx = response.data;
          })
          .catch((error) => {
            parseApiErrorMsg(error.response);
          })
          .then(() => {
          });
    }

    /* 좋아요 취소 */
    const cancelLike = () => {
      axios.delete(process.env.VUE_APP_MODULE_APP_API_URL + props.likeDto.link + props.likeDto.idx,
          {},
      )
          .then((response) => {
            props.likeDto.like = false;
            props.likeDto.likeCount--;
          })
          .catch((error) => {
            parseApiErrorMsg(error.response);
          })
          .then(() => {
          });
    }

    return {
      // function
      like, cancelLike
    }
  }
}
</script>

