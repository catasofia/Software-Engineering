<template>
  <div>Suggestion: {{ sQuestionDetails.suggestion }}</div>
</template>

<script lang="ts">
import OpenAnswerQuestionDetails from '@/models/management/questions/OpenAnswerQuestionDetails';
import { Component, PropSync, Vue } from 'vue-property-decorator';

@Component
export default class OpenAnswerView extends Vue {
  @PropSync('questionDetails', { type: OpenAnswerQuestionDetails })
  sQuestionDetails!: OpenAnswerQuestionDetails;
  //converter suggestion para markdown como no resto das questões
}
</script>
