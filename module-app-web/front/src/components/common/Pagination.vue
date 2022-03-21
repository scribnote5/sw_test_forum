<template>
  <div class="container-fluid">
    <div class="d-flex justify-content-center my-4" aria-label="Page navigation">
      <ul class="pagination ">
        <li class="page-item">
          <a class="page-link" aria-label="Previous"
             @click="searchList({'page': (1)})">&laquo;</a>
        </li>
        <li class="page-item" :style="[first ? {'display': 'none'} : {}]">
          <a class="page-link"
             @click="searchList({'page': (number)})">
            &lsaquo;</a>
        </li>
        <li v-for="page in (endNumber - startNumber + 1)"
            :class="{ 'page-item active': (page + startNumber - 1) === number +1 }">
          <a class="page-link" @click="searchList({'page': (page + startNumber - 1)})">
            <span class="sr-only">{{ page + startNumber - 1 }}</span></a>
        </li>
        <li class="page-item" :style="[last ? {'display': 'none'} : {}]">
          <a class="page-link"
             @click="searchList({'page': (number + 2)})">&rsaquo;</a>
        </li>
        <li class="page-item">
          <a class="page-link" aria-label="Next"
             @click="searchList( {'page': (totalPages)} )">&raquo;</a>
        </li>
      </ul>
    </div>
  </div>
</template>

<style lang="scss">
.page-link {
  color: $lighter-main-blue !important;
  font-weight: $regular-font-weight !important;
}

.page-item.active .page-link {
  background-color: $main-blue !important;
}

.page-item.active .page-link span {
  color: $main-white !important;
  font-weight: $light-font-weight !important;
}
</style>

<script>
export default {
  name: "Pagination",
  props: {
    first: Boolean,
    last: Boolean,
    number: Number,
    startNumber: Number,
    endNumber: Number,
    totalPages: Number,
    searchList: Function
  }
}
</script>

