<template>
  <ul>
    <div>Column One: {{ sQuestionDetails.column }}</div>
    <li v-for="item in questionDetails.columnOne" :key="item.id">
      <span
        v-html="
          convertMarkDown(
            studentAnswered(item.internId) + '**->** ' + item.content
          )
        "
      />
    </li>
    <div>Column Two: {{ sQuestionDetails.column }}</div>
    <li v-for="item in questionDetails.columnTwo" :key="item.id">
      <span
        v-html="
          convertMarkDown(
            studentAnswered(item.internId) + '**->** ' + item.content
          )
        "
      />
    </li>
  </ul>
</template>

<script lang="ts"> 
import ItemCombinationQuestionDetails from '@/models/management/questions/ItemCombinationQuestionDetails';
import { Component, PropSync, Vue } from 'vue-property-decorator';
@Component
export default class ItemCombinationView extends Vue {
  @PropSync('questionDetails', { type: ItemCombinationQuestionDetails })
  sQuestionDetails!: ItemCombinationQuestionDetails;
}
</script>