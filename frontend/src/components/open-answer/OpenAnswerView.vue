<template>
  <div>
    <v-card>Suggestion: {{ sQuestionDetails.suggestion }}</v-card>
    <v-card v-if="answerDetails">Answer: {{ answerDetails.answer }}</v-card>
  </div>
</template>

<script lang="ts">
import OpenAnswerQuestionDetails from '@/models/management/questions/OpenAnswerQuestionDetails';
import { Component, Prop, PropSync, Vue } from 'vue-property-decorator';
import { convertMarkDown } from '@/services/ConvertMarkdownService';
import Image from '@/models/management/Image';
import OpenAnswerAnswerDetails from '@/models/management/questions/OpenAnswerAnswerDetails';

@Component
export default class OpenAnswerView extends Vue {
  @PropSync('questionDetails', { type: OpenAnswerQuestionDetails })
  sQuestionDetails!: OpenAnswerQuestionDetails;
  @Prop() readonly answerDetails?: OpenAnswerAnswerDetails;
  convertMarkDown(text: string, image: Image | null = null): string {
    return convertMarkDown(text, image);
  }
}
</script>
