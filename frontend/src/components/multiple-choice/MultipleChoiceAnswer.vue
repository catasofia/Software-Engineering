<!--
Used on:
  - QuestionComponent.vue
  - ResultComponent.vue
-->

<template>
  <ul data-cy="optionList" class="option-list">
    <li
      v-for="(n, index) in questionDetails.options.length"
      :key="index"
      v-bind:class="['option', optionClass(index)]"
      @click="
        !isReadonly && selectOption(questionDetails.options[index].optionId)
      "
    >
      <span
        v-if="
          isReadonly &&
          correctAnswerDetails.correctOptionsId.indexOf(
            questionDetails.options[index].optionId
          ) > -1
        "
        class="fas fa-check option-letter"
      />
      <span
        v-else-if="
          isReadonly &&
          answerDetails.optionsId.indexOf(
            questionDetails.options[index].optionId
          ) > -1
        "
        class="fas fa-times option-letter"
      />
      <span v-else class="option-letter">{{
        String.fromCharCode(65 + index)
      }}</span>
      <span
        class="option-content"
        v-html="convertMarkDown(questionDetails.options[index].content)"
      />
      <span
        v-if="
          answerDetails.optionsId.length > 0 &&
          answerDetails.optionsId.indexOf(
            questionDetails.options[index].optionId
          ) > -1
        "
        class="option-relevance"
        v-html="
          answerDetails.optionsId.indexOf(
            questionDetails.options[index].optionId
          ) + 1
        "
      />
    </li>
  </ul>
</template>

<script lang="ts">
import { Component, Vue, Prop, Model, Emit } from 'vue-property-decorator';
import MultipleChoiceStatementQuestionDetails from '@/models/statement/questions/MultipleChoiceStatementQuestionDetails';
import { convertMarkDown } from '@/services/ConvertMarkdownService';
import Image from '@/models/management/Image';
import MultipleChoiceStatementAnswerDetails from '@/models/statement/questions/MultipleChoiceStatementAnswerDetails';
import MultipleChoiceStatementCorrectAnswerDetails from '@/models/statement/questions/MultipleChoiceStatementCorrectAnswerDetails';

@Component
export default class MultipleChoiceAnswer extends Vue {
  @Prop(MultipleChoiceStatementQuestionDetails)
  readonly questionDetails!: MultipleChoiceStatementQuestionDetails;
  @Prop(MultipleChoiceStatementAnswerDetails)
  answerDetails!: MultipleChoiceStatementAnswerDetails;
  @Prop(MultipleChoiceStatementCorrectAnswerDetails)
  readonly correctAnswerDetails?: MultipleChoiceStatementCorrectAnswerDetails;

  get isReadonly() {
    return !!this.correctAnswerDetails;
  }

  optionClass(index: number) {
    if (this.isReadonly) {
      if (
        !!this.correctAnswerDetails &&
        this.correctAnswerDetails.correctOptionsId.indexOf(
          this.questionDetails.options[index].optionId
        ) > -1
      ) {
        return 'correct';
      } else if (
        this.answerDetails.optionsId.indexOf(
          this.questionDetails.options[index].optionId
        ) > -1
      ) {
        return 'wrong';
      } else {
        return '';
      }
    } else {
      return this.answerDetails.optionsId.indexOf(
        this.questionDetails.options[index].optionId
      ) > -1
        ? 'selected'
        : '';
    }
  }

  @Emit('question-answer-update')
  selectOption(optionId: number) {
    if (this.answerDetails.optionsId.find((x) => x === optionId)) {
      this.answerDetails.optionsId.splice(
        this.answerDetails.optionsId.indexOf(optionId),
        this.answerDetails.optionsId.length -
          this.answerDetails.optionsId.indexOf(optionId)
      );
    } else {
      this.answerDetails.optionsId.push(optionId);
    }
  }

  convertMarkDown(text: string, image: Image | null = null): string {
    return convertMarkDown(text, image);
  }
}
</script>

<style lang="scss" scoped>
.unanswered {
  .correct {
    .option-content {
      background-color: #333333;
      color: rgb(255, 255, 255) !important;
    }

    .option-letter {
      background-color: #333333 !important;
      color: rgb(255, 255, 255) !important;
    }

    .option-relevance {
      background-color: #333333 !important;
      color: rgb(255, 255, 255) !important;
    }
  }
}

.correct-question {
  .correct {
    .option-content {
      background-color: #299455;
      color: rgb(255, 255, 255) !important;
    }

    .option-letter {
      background-color: #299455 !important;
      color: rgb(255, 255, 255) !important;
    }

    .option-relevance {
      background-color: #299455 !important;
      color: rgb(255, 255, 255) !important;
    }
  }
}

.incorrect-question {
  .wrong {
    .option-content {
      background-color: #cf2323;
      color: rgb(255, 255, 255) !important;
    }

    .option-letter {
      background-color: #cf2323 !important;
      color: rgb(255, 255, 255) !important;
    }

    .option-relevance {
      background-color: #cf2323 !important;
      color: rgb(255, 255, 255) !important;
    }
  }
  .correct {
    .option-content {
      background-color: #333333;
      color: rgb(255, 255, 255) !important;
    }

    .option-letter {
      background-color: #333333 !important;
      color: rgb(255, 255, 255) !important;
    }

    .option-relevance {
      background-color: #333333 !important;
      color: rgb(255, 255, 255) !important;
    }
  }
}
</style>
