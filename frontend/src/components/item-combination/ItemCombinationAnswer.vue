<template>
  <div class="item-combination-answer">
    <v-row
        v-for="(item, index) in sQuestionDetails.columnOne"
        :key="'a' + index"
        data-cy="questionItemsInputA"
        align="center"
        class="mx-0 mt-2"
    >
      <template slot="selection" slot-scope="item">
        <v-chip style="height: 24px">{{
            'Item ' + Number(+item.item.internId)
          }}</v-chip>
      </template>
    <template v-slot:item="{ item, attrs, on }">
      <v-list-item v-on="on" v-bind="attrs" #default="{ active }">
        <v-list-item-action>
          <v-checkbox :input-value="active"></v-checkbox>
        </v-list-item-action>
        <v-list-item-content>
          <v-list-item-title>
            <v-row no-gutters align="center">
              <span>{{ 'Item ' + Number(+item.internId) }}</span>
            </v-row>
          </v-list-item-title>
        </v-list-item-content>
      </v-list-item>
    </template>
      <v-row
          v-for="(item, index) in sQuestionDetails.columnTwo"
          :key="'b' + index"
          data-cy="questionItemsInputA"
          align="center"
          class="mx-0 mt-2"
      >
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
