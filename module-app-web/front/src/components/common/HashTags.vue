<template>
    <span v-if="pageInformation === 'list'">
      <span v-if="isEmpty(hashTags)" class="hash-tag-in-list">
         #
      </span>
      <span v-else v-for="(hashTag, i) in hashTags.split('#')" :key="i">
        <span v-if="i === 0" class="hash-tag-in-list">#{{ hashTag }}</span>
      </span>
    </span>

  <div v-if="pageInformation === 'write' || pageInformation === 'update'">
    <div class="autoComplete_wrapper d-flex justify-content-between">
      <div class="d-flex w100-80px">
        <input type="text" name="hashTag" v-model="hashTag" id="hashTag" class="form-control" placeholder="#태그 이름↵(마지막 문자 # 사용 불가)"/>
      </div>
      <div class="d-flex">
        <button @click="hashTagsClickEvent()" class="btn btn-sm btn-outline-main-blue">등록<img :src="require(`@/assets/images/write-main-blue.svg`)" class="ms-2"></button>
      </div>
    </div>

    <div id="hashTagsWrapper" class="my-2 py-1">
      <input type="hidden" name="hashTags" id="hashTags" model="hashTags" class="form-control" readonly/>

      <span v-if="pageInformation === 'update' && !isEmpty(hashTags)" v-for="(hashTag, i) in hashTags.split('#')" :key="i">
        <span draggable="true" :id="'hashTagData' + i">
          <span :id="'hashTagSpace' + i" class="hash-tag-space"></span>
            <span :id="'hashTagContent' + i" class="hash-tag">
              #{{ hashTag }}<img :id="'cancelFileIcon' + i" src="/x-circle-main-black.svg" class="ms-2" @click="cancelHashTagEvent(i)">
            </span>
        </span>
      </span>
    </div>

    <div id="hashTagsStatementWrapper">
      <img :src="require(`@/assets/images/drag.png`)" style="height: 20px"> &nbsp;<span id="hashTagsStatement">해시태그는 드래그 앤 드랍으로 위치를 변경할 수 있습니다.</span>
    </div>
    <p id="hashTagsErrorMessage" class="error-message"></p>
  </div>

  <div v-if="pageInformation === 'read'">
    <span v-if="isEmpty(hashTags)" class="hash-tag">
        #
    </span>
    <span v-else v-for="(hashTag, i) in hashTags.split('#')" :key="i">
      <span class="hash-tag">#{{ hashTag }}</span>&nbsp;&nbsp;&nbsp;
    </span>
  </div>
</template>

<style lang="scss">
.hash-tag-in-list {
  font-size: $small-font-size;
  background-color: $darkest-main-white;
  color: $the-darkest-main-grey;
  border-radius: .5rem;
  padding: .3rem;
}

.hash-tag {
  background-color: $darkest-main-white;
  color: $the-darkest-main-grey;
  border-radius: .5rem;
  display: inline-block;
  padding: .3rem;
  margin: .2rem 0;
}

.hash-tag-space {
  padding: .25rem .5rem;
}
</style>

<script>
import {onBeforeMount, onBeforeUnmount, ref} from "vue";
import {isEmpty} from "@/utils/empty-util";

export default {
  name: "HashTags",
  props: {
    pageInformation: String,
    hashTags: String
  }, setup(props) {
    let hashTagId = 0;
    const hashTag = ref("");

    onBeforeMount(async () => {
      if (props.pageInformation === 'write' || props.pageInformation === 'update') {
        document.addEventListener("dragstart", dragstart);
        document.addEventListener("dragend", dragend);
        document.addEventListener("dragenter", dragenter);
        document.addEventListener("dragover", dragover);
        document.addEventListener("dragleave", dragleave);
        document.addEventListener("drop", drop);
      }
    });

    onBeforeUnmount(async () => {
      if (props.pageInformation === 'write' || props.pageInformation === 'update') {
        document.removeEventListener("dragstart", dragstart);
        document.removeEventListener("dragend", dragend);
        document.removeEventListener("dragenter", dragenter);
        document.removeEventListener("dragover", dragover);
        document.removeEventListener("dragleave", dragleave);
        document.removeEventListener("drop", drop);
      }
    });

    /* dragstart event */
    const dragstart = (event) => {
      event.dataTransfer.setData("Text", event.target.id);
      document.getElementById("hashTagsStatement").innerHTML = "드래그 앤 드랍으로 위치를 이동 해주세요.";
      document.getElementById("hashTagsStatement").style.color = "red";
      event.target.style.opacity = "0.4";
    }

    /* dragend event */
    const dragend = (event) => {
      document.getElementById("hashTagsStatement").innerHTML = "해시태그는 드래그 앤 드랍으로 위치를 변경할 수 있습니다.";
      document.getElementById("hashTagsStatement").style.color = "black";
      event.target.style.opacity = "1";
    }

    /* dragenter event */
    const dragenter = (event) => {
      let eventTargetId = event.target.id;

      event.preventDefault();

      // hashTagsWrapper인 경우 hashTag가 가장 마지막으로 이동
      if (event.target.id == "hashTagsWrapper") {
        event.target.style.border = "2px dotted red";
      }
      // hashTagSpace인 경우 hashTag를 hashTagSpace 앞으로 이동
      else if (/[0-9]/g.test(eventTargetId) && /hashTagSpace/.test(eventTargetId)) {
        event.target.style.border = "2px dotted red";
      }
    }

    /* dragover event */
    const dragover = (event) => {
      event.preventDefault();
    }

    /* dragleave event */
    const dragleave = (event) => {
      let eventTargetId = event.target.id;
      event.preventDefault();

      // hashTagsWrapper인 경우 hashTag가 가장 마지막으로 이동
      if (event.target.id == "hashTagsWrapper") {
        event.target.style.removeProperty("border");
      }
      // hashTagSpace인 경우 hashTag를 hashTagSpace 앞으로 이동
      else if (/[0-9]/g.test(eventTargetId) && /hashTagSpace/.test(eventTargetId)) {
        event.target.style.removeProperty("border");
      }
    }

    /* drop event */
    const drop = (event) => {
      let eventTargetId = event.target.id;
      let numberIndex;
      let targetIdIndex;

      event.preventDefault();

      // hashTagsWrapper인 경우 hashTag가 가장 마지막으로 이동
      if (event.target.id == "hashTagsWrapper") {
        document.getElementById("hashTagsStatement").style.color = "";
        event.target.style.border = "";
        document.getElementById("hashTagsWrapper").appendChild(document.getElementById(event.dataTransfer.getData("Text")));
      }
      // hashTagSpace인 경우 hashTag를 hashTagSpace 앞으로 이동
      else if (/[0-9]/g.test(eventTargetId) && /hashTagSpace/.test(eventTargetId)) {
        numberIndex = eventTargetId.search(/[0-9]/g);
        targetIdIndex = eventTargetId.substring(numberIndex, eventTargetId.length);
        document.getElementById("hashTagsStatement").style.color = "";
        event.target.style.border = "";
        document.getElementById("hashTagData" + targetIdIndex).before(document.getElementById(event.dataTransfer.getData("Text")));
      }
    }

    /* hash tags click event */
    const hashTagsClickEvent = () => {
      hashTagId = isEmpty(props.hashTags) ? hashTagId : hashTagId + (hashTagId < props.hashTags.split('#').length ? props.hashTags.split('#').length : 0);
      // 기존 정규식(특수 문자 허용 x, 공백만 허용)
      // let regExp = /^[#][a-zA-Zㄱ-힣0-9\s|s]*$/;
      // 기존 정규식(특수 문자 허용, 공백 허용, #으로 끝나는 것 허용 x)
      let regExp = /^[#][a-zA-Zㄱ-힣0-9\s\,\.\!\@\$\%\^\&\*\(\)\-\_\+\=\/\\\||s]*$/;
      let hashTag = document.getElementsByName("hashTag")[0].value;

      if (regExp.test(hashTag)) {
        const tempHashTagId = hashTagId;
        const tag = '<span draggable="true" id="hashTagData' + hashTagId + '">'
            + '<span id="hashTagSpace' + hashTagId + '" class="hash-tag-space">    </span>'
            + '<span id="hashTagContent' + hashTagId + '" class="hash-tag">'
            + hashTag
            + '<img id="cancelFileIcon' + tempHashTagId + '" src="/x-circle-main-black.svg" class="ms-2">'
            + '</span>'
            + '</span>';

        document.getElementById("hashTagsWrapper").insertAdjacentHTML("beforeend", tag);
        document.getElementById("cancelFileIcon" + tempHashTagId).onclick = function () {
          cancelHashTagEvent(tempHashTagId);
        }

        document.getElementById("hashTagsErrorMessage").innerText = "";
        document.getElementsByName("hashTag")[0].value = "";
        hashTagId++;
      } else {
        document.getElementById("hashTagsErrorMessage").innerText = "해시태그를 잘못 입력했습니다.";
        document.getElementsByName("hashTag")[0].focus();
      }
    }


    /* hash tag를 취소하는 경우 */
    const cancelHashTagEvent = (hashTagId) => {
      document.getElementById("hashTagData" + hashTagId).remove();
    }

    return {
      // variable
      hashTag,

      // function
      isEmpty, hashTagsClickEvent, cancelHashTagEvent
    }
  }
}
</script>