import StatementAnswerDetails from '@/models/statement/questions/StatementAnswerDetails';
import StatementCorrectAnswerDetails from '@/models/statement/questions/StatementCorrectAnswerDetails';
import { QuestionTypes } from '@/services/QuestionHelpers';
import ItemCombinationSlotAnswerStatement from '@/models/statement/questions/ItemCombinationSlotAnswerStatement';

export default class ItemCombinationStatementAnswerDetails extends StatementAnswerDetails{
  public answeredSlots!: ItemCombinationSlotAnswerStatement[];

  constructor(jsonObj?: ItemCombinationStatementAnswerDetails) {
    super(QuestionTypes.ItemCombination);
    if (jsonObj) {
      this.answeredSlots = jsonObj.answeredSlots || [];
    }
  }

  isAnswerCorrect(correctAnswerDetails: StatementCorrectAnswerDetails): boolean {
    return true;
  }

  isQuestionAnswered(): boolean {
    return this.answeredSlots != null && this.answeredSlots.length > 0;
  }
}
