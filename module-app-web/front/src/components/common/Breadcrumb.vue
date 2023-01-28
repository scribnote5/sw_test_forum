<template>
  <div class="container-fluid">
    <div class="page-header breadcrumbs">
      <div class="d-flex justify-content-between align-items-center">
        <div class="page-wrapper">
          <h2>{{ page }}</h2>
          <h6 class="mt-2 ms-2">{{ subPage }}</h6>
        </div>
        <div class="path-wrapper">
          <div v-if="!isEmpty(title)" class="print-pdf-wrapper">
            <button @click="printPdf()" class="btn btn-sm btn-main-light-ultra-marine d-flex align-items-center ms-auto">pdf 출력<img :src="require(`@/assets/images/printer-white.svg`)" class="ms-2"></button>
          </div>
          <ol :class="{ 'title-padding-true': !isEmpty(title), 'title-padding-false': isEmpty(title) }">
            <li v-for="(path, i) in paths" :key="i">
              {{ path }}
            </li>
          </ol>
        </div>
      </div>
    </div>
  </div>
</template>

<style lang="scss">
.breadcrumbs {
  padding: 20px;
  //max-height: 150px;
  min-height: 115px;
  background-color: $dark-custom-grey;
}

.breadcrumbs .page-wrapper {

}

.breadcrumbs h2 {
  font-weight: $strong-font-weight;
  color: $dark-main-white;
}

.breadcrumbs h6 {
  font-weight: $strong-font-weight;
  color: $dark-custom-white;
}

.breadcrumbs .path-wrapper {
  min-width: 300px;
}

.breadcrumbs ol {
  display: flex;
  justify-content: flex-end;
  flex-wrap: wrap;
  list-style: none;
  margin: 0;
}

.breadcrumbs .title-padding-true {
  padding-top: 20px;
}

.breadcrumbs .title-padding-false {
  padding-top: 50px;
}

.breadcrumbs ol li {
  color: $darkest-main-white;
}

.breadcrumbs ol li + li {
  padding-left: 7px;
}

.breadcrumbs ol li + li::before {
  display: inline-block;
  padding-right: 7px;
  color: rgba(250, 250, 250, 0.5);
  content: "〉";
  font-weight: $strong-font-weight;
}

@media (max-width: 768px) {
  .breadcrumbs .d-flex {
    display: block !important;
  }

  .breadcrumbs .print-pdf-wrapper {
    display: block;
  }

  .breadcrumbs h2 {
    font-size: 1.65rem;
  }

  .breadcrumbs h6 {
    font-size: .85rem;
  }

  .breadcrumbs ol li {
    display: block;
    font-size: $smaller-font-size;
  }

  .breadcrumbs .title-padding-false {
    padding-top: 10px;
  }

  .breadcrumbs .title-padding-true {
    padding-top: 7px;
  }
}
</style>

<script>
import {jsPDF} from "jspdf";
import html2canvas from "html2canvas";

// utils
import {isEmpty} from "@/utils/empty-util";

export default {
  name: "Breadcrumb",
  components: {},
  props: {
    page: String,
    subPage: String,
    paths: Array,
    title: String,
  },
  setup(props) {
    const printPdf = async () => {
      document.getElementById("loading-wrapper").style.visibility = "visible";
      await createPdf();
      document.getElementById("loading-wrapper").style.visibility = "hidden";
    }

    const createPdf = () => {
      return new Promise((resolve, reject) => {
        html2canvas(document.getElementsByClassName("page-content")[0],
            {
              logging: false,
              allowTaint: true,
              useCORS: true,
              scale: 3			// 기본 해상도 3배 증가
            }
        ).then(canvas => {
          let filename = 'OTA-REPORT_' + Date.now() + '.pdf';
          let doc = new jsPDF('p', 'mm', 'a4');
          let imgData = canvas.toDataURL('image/png');
          let imgWidth = 200; // A4: 210
          let pageHeight = 297; // A4: 297
          let imgHeight = (canvas.height * imgWidth / canvas.width) - 10;
          let heightLeft = imgHeight;
          let position = 10;

          doc.addImage(imgData, 'png', 5, position, imgWidth, imgHeight);
          heightLeft -= pageHeight;

          while (heightLeft >= 0) {
            position = heightLeft - imgHeight + 10;
            doc.addPage();
            doc.addImage(imgData, 'png', 5, position, imgWidth, imgHeight);
            heightLeft -= pageHeight;
          }
          doc.save(props.title);
          resolve(true);
        });
      });
    }

    return {
      // function
      printPdf, isEmpty
    }
  }
}
</script>