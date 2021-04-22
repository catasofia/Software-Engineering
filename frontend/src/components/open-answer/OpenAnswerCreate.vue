<template>
  <div class="open-answer-create">
    <v-textarea
      v-model="sQuestionDetails.suggestion"
      :label="'Suggestion'"
      rows="1"
      auto-grow
      data-cy="questionSuggestionTextArea"
    ></v-textarea>
  </div>
</template>

<script lang="ts">
import { Component, Vue, Prop, PropSync } from 'vue-property-decorator';
import OpenAnswerQuestionDetails from '@/models/management/questions/OpenAnswerQuestionDetails';

@Component
export default class OpenAnswerCreate extends Vue {
  @PropSync('questionDetails', { type: OpenAnswerQuestionDetails })
  sQuestionDetails!: OpenAnswerQuestionDetails;
  @Prop({ default: true }) readonly readonlyEdit!: boolean;
}
</script>

<style lang="scss">
.open-answer-create .CodeMirror {
  height: auto;
}
</style>
