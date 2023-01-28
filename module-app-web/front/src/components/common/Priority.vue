<template>
  <span v-if="pageInformation === 'read'">
    <span v-if="priority < maxPriority">{{ priority }}</span>
    <span v-else>우선순위가 설정되어 있지 않습니다.</span><br>
  </span>
  <div v-if="pageInformation === 'write' || pageInformation === 'update'">
    <select name="priority" id="priority" class="form-control">
      <option v-for="(priorityElement, i) in priorityArray" :key="i" :value="i + 1" :disabled="priorityElement !== null ? priorityElement.disabled : false" :selected="priority == i + 1">
        <span v-if="priorityElement === null">{{ i + 1 }}</span>
        <span v-else-if="priorityElement !== null && i+1 !== maxPriority">{{ i + 1 }}. {{ priorityElement.note }}</span>
        <span v-else>{{ priorityElement.note }}</span>
      </option>
    </select>
  </div>
</template>

<style lang="scss">

</style>

<script>
export default {
  name: "Priority",
  props: {
    pageInformation: String,
    priority: Number,
    maxPriority: Number,
    priorityArray: Object
  }
}
</script>