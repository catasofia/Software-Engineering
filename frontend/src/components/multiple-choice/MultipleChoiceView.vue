<template>
  <ul>
    <li v-for="option in questionDetails.options" :key="option.id">
      <span
        v-if="option.correct && option.relevance > 0"
        v-html="
          convertMarkDown(
            studentAnswered(option.id) +
              '**[★]** **[' +
              option.relevance +
              ']** ' +
              option.content
          )
        "
      />
      <span
        v-else-if="option.correct && option.relevance <= 0"
        v-html="
          convertMarkDown(
            studentAnswered(option.id) + '**[★]** ' + option.content
          )
        "
        v-bind:class="[option.correct ? 'font-weight-bold' : '']"
      />
      <span
        v-else
        v-html="convertMarkDown(studentAnswered(option.id) + option.content)"
      />
    </li>
  </ul>
</template>

<script lang="ts">
import { Component, Vue, Prop } from 'vue-property-decorator';
import { convertMarkDown } from '@/services/ConvertMarkdownService';
import Question from '@/models/management/Question';
import Image from '@/models/management/Image';
import MultipleChoiceQuestionDetails from '@/models/management/questions/MultipleChoiceQuestionDetails';
import MultipleChoiceAnswerDetails from '@/models/management/questions/MultipleChoiceAnswerDetails';

@Component
export default class MultipleChoiceView extends Vue {
  @Prop() readonly questionDetails!: MultipleChoiceQuestionDetails;
  @Prop() readonly answerDetails?: MultipleChoiceAnswerDetails;

  studentAnswered(option: number) {
    if (this.answerDetails) {
      let studentRelevance =
        this.answerDetails.option.map((x) => x.id).indexOf(option) + 1;
      if (studentRelevance >= 0) return '**[S - ' + studentRelevance + ']** ';
    }
    return '';
  }

  convertMarkDown(text: string, image: Image | null = null): string {
    return convertMarkDown(text, image);
  }
}
</script>
