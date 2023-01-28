<template>
  <body v-if="this.$route.meta.layoutView === true">
  <Header/>
  <main>
    <Sidebar/>
    <router-view/>
  </main>
  <Footer/>
  </body>

  <body v-else>
  <router-view/>
  </body>
</template>

<style lang="scss">
main {
  display: flex;
  flex-wrap: nowrap;
  overflow-x: hidden;
  overflow-y: hidden;

  // header, sidebar fixed
  margin-left: 275px;
  margin-top: 55px;

  @media (min-width: 768px) {
    margin-left: 275px !important;
  }

  @media (max-width: 767px) {
    margin-left: 0px !important;
  }
}
</style>

<script>
import Header from '@/router/layout/Header'
import Sidebar from '@/router/layout/Sidebar'
import Footer from '@/router/layout/Footer'

export default ({
  name: 'App',
  components: {Header, Sidebar, Footer},
})
</script>
