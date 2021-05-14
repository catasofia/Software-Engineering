<template>
  <div class="item-combination-answer">
    <v-row justify="start" class="mt-2">
      <v-col cols="12" style="text-align: left">
        <h2>Coluna A</h2>
      </v-col>
    </v-row>
    <v-row class="mt-12">
      <v-col cols="12" style="text-align: left">
        <h2>Coluna B</h2>
      </v-col>
    </v-row>
  </div>
</template>

<script lang="ts">
import { Component, Vue, Prop, Model, Emit } from 'vue-property-decorator';
import { convertMarkDown } from '@/services/ConvertMarkdownService';
import Image from '@/models/management/Image';
import ItemCombinationStatementQuestionDetails from '@/models/statement/questions/ItemCombinationStatementQuestionDetails';
import ItemCombinationStatementCorrectAnswerDetails from '@/models/statement/questions/ItemCombinationStatementCorrectAnswerDetails';

@Component
export default class ItemCombinationAnswer extends Vue {
  @Prop(ItemCombinationStatementQuestionDetails)
  readonly questionDetails!: ItemCombinationStatementQuestionDetails;
  @Prop(ItemCombinationStatementCorrectAnswerDetails)
  answerDetails!: ItemCombinationStatementCorrectAnswerDetails;
  @Prop(ItemCombinationStatementCorrectAnswerDetails)
  readonly correctAnswerDetails?: ItemCombinationStatementCorrectAnswerDetails;

  get isReadonly() {
    return !!this.correctAnswerDetails;
  }

  /*itemClass(index: number) {
    if (this.isReadonly) {
      if (
        !!this.correctAnswerDetails &&
        this.correctAnswerDetails.correctOptionId ===
          this.questionDetails.options[index].optionId
      ) {
        return 'correct';
      } else if (
        this.answerDetails.optionId ===
        this.questionDetails.options[index].optionId
      ) {
        return 'wrong';
      } else {
        return '';
      }
    } else {
      return this.answerDetails.optionId ===
        this.questionDetails.options[index].optionId
        ? 'selected'
        : '';
    }
  }*/

  /*@Emit('question-answer-update')
  selectOption(optionId: number) {
    if (this.answerDetails.optionId === optionId) {
      this.answerDetails.optionId = null;
    } else {
      this.answerDetails.optionId = optionId;
    }
  }*/

  convertMarkDown(text: string, image: Image | null = null): string {
    return convertMarkDown(text, image);
  }
}
</script>

<!---<style lang="scss" scoped>
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
  }
}
</style>--->
