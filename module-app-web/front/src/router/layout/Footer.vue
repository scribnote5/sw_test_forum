<!-- https://getbootstrap.com/docs/5.0/examples/sticky-footer/ -->
<template>
  <footer id="footer" class="py-3 px-2">
    <div class="container">
      <div>
        <span class="text-muted">© Copyright by <strong>scribnote5</strong>. All Rights Reserved</span>
      </div>
    </div>
  </footer>
</template>

<style lang="scss">
#footer {
  position: relative;
  left: 0;
  bottom: 0;
  width: 100%;
  text-align: center;
  background-color: $darker-custom-grey;
  height: 50px;

  strong {
    color: $lighter-main-grey;
  }
}

@media (max-width: 768px) {
  #footer {
    .text-muted {
      font-size: $small-font-size;
    }

    .text-muted strong {
      font-size: $small-font-size;
    }
  }
}

.container {
  padding: 0 15px;
}
</style>