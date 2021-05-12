<template>
  <div class="open-answer-answer">
    <v-textarea
      v-model="answerDetails.answer"
      :label="'Answer'"
      rows="1"
      auto-grow
      data-cy="questionSuggestionTextArea"
    ></v-textarea>
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
export default class OpenAnswerAnswer extends Vue {
  @Prop(OpenAnswerStatementQuestionDetails)
  readonly questionDetails!: OpenAnswerStatementQuestionDetails;
  @Prop(OpenAnswerStatementAnswerDetails)
  answerDetails!: OpenAnswerStatementAnswerDetails;
  @Prop(OpenAnswerStatementCorrectAnswerDetails)
  readonly correctAnswerDetails?: OpenAnswerStatementCorrectAnswerDetails;

  convertMarkDown(text: string, image: Image | null = null): string {
    return convertMarkDown(text, image);
  }
}
</script>
