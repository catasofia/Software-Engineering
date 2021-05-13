<template>
  <div Correct combinations:>
    <h2 class="mb-2">Coluna A</h2>
    <ul>
      <li v-for="item in questionDetails.columnOne" :key="item.id">
        {{item.content + combinesWith(item.correctCombinations)}}
      </li>
    </ul>
    <h2 class="mt-5 mb-2">Coluna B</h2>
    <ul>
      <li v-for="item in questionDetails.columnTwo" :key="item.id">
        {{item.content}}
      </li>
    </ul>
  </div>
</template>

<script lang="ts">
import { Component, Vue, Prop } from 'vue-property-decorator';
import ItemCombinationQuestionDetails from '@/models/management/questions/ItemCombinationQuestionDetails';
import ItemCombinationAnswerDetails from '@/models/management/questions/ItemCombinationAnswerDetails';

@Component
export default class ItemCombinationView extends Vue {
  @Prop() readonly questionDetails!: ItemCombinationQuestionDetails;
  @Prop() readonly answerDetails?: ItemCombinationAnswerDetails;

  /*studentAnswered(internId: number) {
    return this.answerDetails && this.answerDetails?.itemCombinationSlot.internId === internId
      ? '**[S]** '
      : '';
  }*/

  combinesWith(combinations: any[]) {
    if(combinations.length) {
      let s = ' combines with '
      for(let combination of combinations) {
        s += combination.content + ', ';
      }
      return s.substr(0, s.length - 2);
    } else {
      return ''
    }
  }
}
</script>