<template>
  <span v-if="pageInformation === 'list'" class="frequency">
    <span class="custom-tooltip me-1">
      <span v-if="frequency === 'HIGH'">
        <img :src="require(`@/assets/images/high-frequency.png`)" class="tooltip-right">
        <span class="tooltip-text">위배 빈도: 높음</span>
      </span>
      <span v-if="frequency === 'AVERAGE'">
        <img :src="require(`@/assets/images/average-frequency.png`)">
        <span class="tooltip-text">위배 빈도: 보통</span>
      </span>
      <span v-if="frequency === 'LOW'">
        <img :src="require(`@/assets/images/low-frequency.png`)">
        <span class="tooltip-text">위배 빈도: 낮음</span>
      </span>
    </span>
  </span>
  <span v-if="pageInformation === 'read'" class="frequency">
    <span class="custom-tooltip">
      <span v-if="frequency === 'HIGH'">
        <img :src="require(`@/assets/images/high-frequency.png`)">
        <span class="tooltip-text">위배 빈도: 높음</span>
      </span>
      <span v-if="frequency === 'AVERAGE'">
        <img :src="require(`@/assets/images/average-frequency.png`)">
        <span class="tooltip-text">위배 빈도: 보통</span>
      </span>
      <span v-if="frequency === 'LOW'">
        <img :src="require(`@/assets/images/low-frequency.png`)">
        <span class="tooltip-text">위배 빈도: 낮음</span>
      </span>
    </span>
    <br>
  </span>
  <div v-if="pageInformation === 'write' || pageInformation === 'update'">
    <select name="frequency" id="frequency" class="form-select" :value="frequency">
      <option value="HIGH">높음</option>
      <option value="AVERAGE">보통</option>
      <option value="LOW">낮음</option>
    </select>
  </div>
</template>

<style lang="scss">
.frequency {
  margin-left: 2px;
  margin-right: 5px;

  img {
    margin-bottom: 2px;
  }
}
</style>

<script>
export default {
  name: "Frequency",
  props: {
    pageInformation: String,
    frequency: String
  }
}
</script>

