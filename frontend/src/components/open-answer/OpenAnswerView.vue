<template>
  <div>Suggestion: {{ sQuestionDetails.suggestion }}</div>
</template>

<script lang="ts">
import OpenAnswerQuestionDetails from '@/models/management/questions/OpenAnswerQuestionDetails';
import { Component, PropSync, Vue } from 'vue-property-decorator';
import { convertMarkDown } from '@/services/ConvertMarkdownService';
import Image from '@/models/management/Image';

@Component
export default class OpenAnswerView extends Vue {
  @PropSync('questionDetails', { type: OpenAnswerQuestionDetails })
  sQuestionDetails!: OpenAnswerQuestionDetails;

  convertMarkDown(text: string, image: Image | null = null): string {
    return convertMarkDown(text, image);
  }
}
</script>
