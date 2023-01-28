<template>
  <div>
    <div v-if="pageInformation === 'write' || pageInformation === 'update'">
      <div class="mb-4">
        <div class="d-flex flex-column flex-lg-row">
          <div class="w-100 me-3 mb-3 mb-lg-0" style="overflow:hidden">
            <div class="d-flex">
              <div class="my-2">
                <strong>Bad Case 코드</strong>
                <button @click="badCaseHighlight" class="btn btn-sm btn-bad-case ms-3 me-2">하이라이트<img :src="require(`@/assets/images/update-white.svg`)" class="ms-2"></button>
                <button @click="badCaseHighlightDelete" class="btn btn-sm btn-delete-case">되돌리기<img :src="require(`@/assets/images/rotate-ccw-white.svg`)" class="ms-2"></button>
              </div>
            </div>
            <textarea name="nonCompliantExample" id="nonCompliantExample" v-model=nonCompliantExample :placeholder="codeMirror.editorData"/>
            <p id="nonCompliantExampleErrorMessage" class="error-message"></p>
          </div>
          <div class="w-100 mb-3 mb-lg-0" style="overflow:hidden">
            <div class="my-2">
              <strong>Good Case 코드</strong>
              <button @click="goodCaseHighlight" class="btn btn-sm btn-good-case ms-3 me-2">하이라이트<img :src="require(`@/assets/images/update-white.svg`)" class="ms-2"></button>
              <button @click="goodCaseHighlightDelete" class="btn btn-sm btn-delete-case">되돌리기<img :src="require(`@/assets/images/rotate-ccw-white.svg`)" class="ms-2"></button>
            </div>
            <textarea name="compliantExample" id="compliantExample" v-model=compliantExample :placeholder="codeMirror.editorData"/>
            <p id="compliantExampleErrorMessage" class="error-message"></p>
          </div>
        </div>
      </div>
    </div>

    <div v-if="pageInformation === 'read'">
      <div class="mb-4">
        <div class="d-flex flex-column flex-lg-row">
          <div class="w-100 me-3 mb-3 mb-lg-0" style="overflow:hidden">
            <p class="mt-1"><strong>Bad Case 코드</strong></p>
            <textarea name="nonCompliantExampleWhenRead" id="nonCompliantExampleWhenRead" v-model="nonCompliantExampleWhenRead" :placeholder="codeMirror.editorData"/>
          </div>
          <div class="w-100 mb-3 mb-lg-0" style="overflow:hidden">
            <p class="mt-1"><strong>Good Case 코드</strong></p>
            <textarea name="compliantExampleWhenRead" id="compliantExampleWhenRead" v-model="compliantExampleWhenRead" :placeholder="codeMirror.editorData"/>
          </div>
        </div>
      </div>
    </div>

    <div v-if="pageInformation === 'rule-page-read'">
      <div id="codeMirrorWrapper0" class="mb-4">
        <h5 v-if="!isEmpty(exampleList[0])">
          <router-link :to="'' + exampleList[0].link">{{ exampleList[0].title }} <img :src="require(`@/assets/images/external-link-main-blue.svg`)" class="ms-1"></router-link>
        </h5>
        <div class="d-flex flex-column flex-lg-row">
          <div class="w-100 me-3 mb-3 mb-lg-0" style="overflow:hidden">
            <p class="mt-1"><strong>Bad Case 코드</strong></p>
            <textarea name="nonCompliantExampleWhenRulePageRead" id="nonCompliantExampleWhenRulePageRead0" :placeholder="codeMirror.editorData"/>
          </div>
          <div class="w-100 mb-3 mb-lg-0" style="overflow:hidden">
            <p class="mt-1"><strong>Good Case 코드</strong></p>
            <textarea name="compliantExampleWhenRulePageRead" id="compliantExampleWhenRulePageRead0" :placeholder="codeMirror.editorData"/>
          </div>
        </div>
      </div>

      <div id="codeMirrorWrapper1" class="mb-4">
        <h5 v-if="!isEmpty(exampleList[1])">
          <router-link :to="'' + exampleList[1].link">{{ exampleList[1].title }} <img :src="require(`@/assets/images/external-link-main-blue.svg`)" class="ms-1"></router-link>
        </h5>
        <div class="d-flex flex-column flex-lg-row">
          <div class="w-100 me-3 mb-3 mb-lg-0" style="overflow-y:hidden">
            <p class="mt-1"><strong>Bad Case 코드</strong></p>
            <textarea name="nonCompliantExampleWhenRulePageRead" id="nonCompliantExampleWhenRulePageRead1" :placeholder="codeMirror.editorData"/>
          </div>
          <div class="w-100 mb-3 mb-lg-0" style="overflow:hidden">
            <p class="mt-1"><strong>Good Case 코드</strong></p>
            <textarea name="compliantExampleWhenRulePageRead" id="compliantExampleWhenRulePageRead1" :placeholder="codeMirror.editorData"/>
          </div>
        </div>
      </div>

      <div id="codeMirrorWrapper2" class="mb-4">
        <h5 v-if="!isEmpty(exampleList[2])">
          <router-link :to="'' + exampleList[2].link">{{ exampleList[2].title }} <img :src="require(`@/assets/images/external-link-main-blue.svg`)" class="ms-1"></router-link>
        </h5>
        <div class="d-flex flex-column flex-lg-row">
          <div class="w-100 me-3 mb-3 mb-lg-0" style="overflow:hidden">
            <p class="mt-1"><strong>Bad Case 코드</strong></p>
            <textarea name="nonCompliantExampleWhenRulePageRead" id="nonCompliantExampleWhenRulePageRead2" :placeholder="codeMirror.editorData"/>
          </div>
          <div class="w-100 mb-3 mb-lg-0" style="overflow:hidden">
            <p class="mt-1"><strong>Good Case 코드</strong></p>
            <textarea name="compliantExampleWhenRulePageRead" id="compliantExampleWhenRulePageRead2" :placeholder="codeMirror.editorData"/>
          </div>
        </div>
      </div>

      <div v-if="exampleList.length === 0">
        <div class="no-posts-div border-bottom">
          등록된 규칙 예제가 없습니다.
        </div>
      </div>

      <div class="d-flex justify-content-end me-4 my-4">
        <router-link :to="link">
          <button class="btn btn-main-orange d-flex align-items-center">더보기<img :src="require(`@/assets/images/more-horizontal-white.svg`)" class="ms-2"></button>
        </router-link>
      </div>
    </div>
  </div>
</template>

<style lang="scss">
// CodeMirror
@import '~@/assets/css/codemirror.css';
@import '~codemirror/theme/eclipse.css';
@import '~codemirror/theme/dracula.css';

.styled-background {
  background-color: red;
}
</style>

<script>
// vue.js
import {onBeforeUpdate, onMounted, ref} from "vue";
// CodeMirror
import CodeMirror from 'codemirror/lib/codemirror.js';
import 'codemirror/mode/clike/clike.js';
import 'codemirror/addon/display/placeholder.js';
import {codeMirror} from '@/assets/plugins/code-mirror/code-mirror.js'

// utils
import {isEmpty} from "@/utils/empty-util";

export default {
  name: "CodeMirror",
  props: {
    pageInformation: String,
    nonCompliantExampleValue: String,
    compliantExampleValue: String,
    badCasePositionList: String,
    goodCasePositionList: String,
    exampleList: Array,
    link: String,
    mode: String,
  }, setup(props) {
    const rulePageReadLength = 3;
    let nonCompliantExample = ref("");
    let compliantExample = ref("");
    let nonCompliantExampleWhenRead = ref("");
    let compliantExampleWhenRead = ref("");
    let badCasePosition = [];
    let goodCasePosition = [];

    const writeAndReadOption = {
      lineNumbers: true,
      lineWrapping: true,
      indentWithTabs: true,
      indentUnit: 4,
      viewportMargin: Infinity,
      mode: props.mode,
      theme: 'eclipse'
    }

    const readOption = {
      readOnly: true,
      lineNumbers: true,
      indentUnit: 4,
      mode: props.mode,
      theme: 'eclipse',
    }

    onMounted(async () => {
      codeMirror.initCodeMirror();
      if (props.pageInformation === "write") {
        nonCompliantExample = CodeMirror.fromTextArea(document.getElementsByName('nonCompliantExample')[0], writeAndReadOption);
        // CodeMirror 에디터에서 커서가 사라지는 경우 데이터 저장
        nonCompliantExample.on("blur", () => {
          codeMirror.setNonCompliantExample(nonCompliantExample.getValue());
        });
        nonCompliantExample.save();

        compliantExample = CodeMirror.fromTextArea(document.getElementsByName('compliantExample')[0], writeAndReadOption);
        // CodeMirror 에디터에서 커서가 사라지는 경우 데이터 저장
        compliantExample.on("blur", () => {
          codeMirror.setCompliantExample(compliantExample.getValue());
        });
        compliantExample.save();
      }
    });

    onBeforeUpdate(async () => {
      if (props.pageInformation === "read") {
        nonCompliantExampleWhenRead = CodeMirror.fromTextArea(document.getElementsByName('nonCompliantExampleWhenRead')[0], readOption);
        nonCompliantExampleWhenRead.getDoc().setValue(props.nonCompliantExampleValue);
        for (const position of JSON.parse(props.badCasePositionList)) {
          nonCompliantExampleWhenRead.markText({line: position[0], ch: position[1]}, {line: position[2], ch: position[3]}, {className: "bad-case-highlight"});
        }
        nonCompliantExampleWhenRead.save();

        compliantExampleWhenRead = CodeMirror.fromTextArea(document.getElementsByName('compliantExampleWhenRead')[0], readOption);
        compliantExampleWhenRead.getDoc().setValue(props.compliantExampleValue);
        for (const position of JSON.parse(props.goodCasePositionList)) {
          compliantExampleWhenRead.markText({line: position[0], ch: position[1]}, {line: position[2], ch: position[3]}, {className: "good-case-highlight"});
        }

        compliantExampleWhenRead.save();
      } else if (props.pageInformation === "rule-page-read") {
        for (let i = 0; i < props.exampleList.length; i++) {
          let nonCompliantExampleWhenRulePageRead = CodeMirror.fromTextArea(document.getElementsByName('nonCompliantExampleWhenRulePageRead')[i], readOption);
          nonCompliantExampleWhenRulePageRead.getDoc().setValue(props.exampleList[i].nonCompliantExample);
          for (const position of props.exampleList[i].badCasePositionList) {
            nonCompliantExampleWhenRulePageRead.markText({line: position[0], ch: position[1]}, {line: position[2], ch: position[3]}, {className: "bad-case-highlight"});
          }
          nonCompliantExampleWhenRulePageRead.save();

          let compliantExampleWhenRulePageRead = CodeMirror.fromTextArea(document.getElementsByName('compliantExampleWhenRulePageRead')[i], readOption);
          compliantExampleWhenRulePageRead.getDoc().setValue(props.exampleList[i].compliantExample);
          for (const position of props.exampleList[i].goodCasePositionList) {
            compliantExampleWhenRulePageRead.markText({line: position[0], ch: position[1]}, {line: position[2], ch: position[3]}, {className: "good-case-highlight"});
          }
          compliantExampleWhenRulePageRead.save();
        }

        // 값이 할당되지 않은 codeMirror는 display: none으로 설정
        for (let i = 0; i < rulePageReadLength - props.exampleList.length; i++) {
          document.getElementById("codeMirrorWrapper" + (i + props.exampleList.length)).setAttribute("style", "display: none !important");
        }
      } else if (props.pageInformation === "update") {
        nonCompliantExample = CodeMirror.fromTextArea(document.getElementsByName('nonCompliantExample')[0], writeAndReadOption);
        // CodeMirror 에디터에서 커서가 사라지는 경우 데이터 저장
        nonCompliantExample.on("blur", () => {
          codeMirror.setNonCompliantExample(nonCompliantExample.getValue());
        });
        nonCompliantExample.getDoc().setValue(props.nonCompliantExampleValue);
        for (const position of JSON.parse(props.badCasePositionList)) {
          nonCompliantExample.markText({line: position[0], ch: position[1]}, {line: position[2], ch: position[3]}, {className: "bad-case-highlight"});
        }
        nonCompliantExample.save();

        compliantExample = CodeMirror.fromTextArea(document.getElementsByName('compliantExample')[0], writeAndReadOption);
        // CodeMirror 에디터에서 커서가 사라지는 경우 데이터 저장
        compliantExample.on("blur", () => {
          codeMirror.setCompliantExample(compliantExample.getValue());
        });
        compliantExample.getDoc().setValue(props.compliantExampleValue);
        for (const position of JSON.parse(props.goodCasePositionList)) {
          compliantExample.markText({line: position[0], ch: position[1]}, {line: position[2], ch: position[3]}, {className: "good-case-highlight"});
        }
        compliantExample.save();
      }
    });

    // badCase highlight
    const badCaseHighlight = () => {
      nonCompliantExample.markText(nonCompliantExample.getCursor(true), nonCompliantExample.getCursor(false), {className: "bad-case-highlight"});
      badCasePosition.splice(0, badCasePosition.length);

      for (const marker of nonCompliantExample.getAllMarks()) {
        badCasePosition.push([marker.find().from.line, marker.find().from.ch, marker.find().to.line, marker.find().to.ch]);
      }

      // 블럭 지정 해제
      nonCompliantExample.setCursor({line: 0, ch: 0});
      codeMirror.setBadCasePositionList(badCasePosition);
    };

    // badCase highlight 삭제
    const badCaseHighlightDelete = () => {
      nonCompliantExample.findMarks(nonCompliantExample.getCursor(true), nonCompliantExample.getCursor(false)).forEach(marker => marker.clear());
      badCasePosition.splice(0, badCasePosition.length);

      for (const marker of nonCompliantExample.getAllMarks()) {
        badCasePosition.push([marker.find().from.line, marker.find().from.ch, marker.find().to.line, marker.find().to.ch]);
      }

      // 블럭 지정 해제
      nonCompliantExample.setCursor({line: 0, ch: 0});
      codeMirror.setBadCasePositionList(badCasePosition);
    };

    // goodCase highlight
    const goodCaseHighlight = () => {
      compliantExample.markText(compliantExample.getCursor(true), compliantExample.getCursor(false), {className: "good-case-highlight"});
      goodCasePosition.splice(0, goodCasePosition.length);

      for (const marker of compliantExample.getAllMarks()) {
        goodCasePosition.push([marker.find().from.line, marker.find().from.ch, marker.find().to.line, marker.find().to.ch]);
      }

      // 블럭 지정 해제
      compliantExample.setCursor({line: 0, ch: 0});
      codeMirror.setGoodCasePositionList(goodCasePosition);
    };

    // goodCase highlight 삭제
    const goodCaseHighlightDelete = () => {
      compliantExample.findMarks(compliantExample.getCursor(true), compliantExample.getCursor(false)).forEach(marker => marker.clear());
      goodCasePosition.splice(0, goodCasePosition.length);
      for (const marker of compliantExample.getAllMarks()) {
        goodCasePosition.push([marker.find().from.ch, marker.find().from.line, marker.find().to.ch, marker.find().to.line]);
      }

      // 블럭 지정 해제
      compliantExample.setCursor({line: 0, ch: 0});
      codeMirror.setGoodCasePositionList(goodCasePosition);
    };

    return {
      // variable
      CodeMirror,
      codeMirror, nonCompliantExample, compliantExample, nonCompliantExampleWhenRead, compliantExampleWhenRead,

      // function
      isEmpty, badCaseHighlight, goodCaseHighlight, badCaseHighlightDelete, goodCaseHighlightDelete
    }
  }
}
</script>