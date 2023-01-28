<template>
  <div class="container-fluid">
    <div id="loading-wrapper">
      <img :src="require(`@/assets/images/loading.gif`)" id="loading" alt="Loading">
    </div>
  </div>
</template>

<style lang="scss">

#loading-wrapper {
  position: fixed;
  left: 50%;
  top: 50%;
  transform: translate(-50%, -50%);
  z-index: 1000;
  visibility: hidden;
}
</style>

<script>

export default {
  name: "Loading"
}
</script>