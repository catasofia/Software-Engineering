<template>
  <div>
    <v-card>Answer: {{ answerDetails.answer }}</v-card>
    <v-card>Correct Answer: {{ questionDetails.answer }}</v-card>
  </div>
</template>

<script lang="ts">
import { Component, Vue, Prop } from 'vue-property-decorator';
import OpenAnswerStatementQuestionDetails from '@/models/statement/questions/OpenAnswerStatementQuestionDetails';
import { convertMarkDown } from '@/services/ConvertMarkdownService';
import Image from '@/models/management/Image';
import OpenAnswerStatementAnswerDetails from '@/models/statement/questions/OpenAnswerStatementAnswerDetails';
import OpenAnswerStatementCorrectAnswerDetails from '@/models/statement/questions/OpenAnswerStatementCorrectAnswerDetails';

@Component
export default class OpenAnswerResult extends Vue {
  @Prop(OpenAnswerStatementQuestionDetails)
  readonly questionDetails!: OpenAnswerStatementQuestionDetails;
  @Prop(OpenAnswerStatementAnswerDetails)
  readonly answerDetails!: OpenAnswerStatementAnswerDetails;
  @Prop(OpenAnswerStatementCorrectAnswerDetails)
  readonly correctAnswerDetails?: OpenAnswerStatementCorrectAnswerDetails;

  convertMarkDown(text: string, image: Image | null = null): string {
    return convertMarkDown(text, image);
  }
}
</script>
