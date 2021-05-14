import StatementCorrectAnswerDetails from '@/models/statement/questions/StatementCorrectAnswerDetails';
import { QuestionTypes } from '@/services/QuestionHelpers';
import ItemCombinationSlotAnswerStatement from '@/models/statement/questions/ItemCombinationSlotAnswerStatement';

export default class ItemCombinationStatementCorrectAnswerDetails extends StatementCorrectAnswerDetails {
  public correctCombinations!: ItemCombinationSlotAnswerStatement[];

  constructor(jsonObj?: ItemCombinationStatementCorrectAnswerDetails) {
    super(QuestionTypes.ItemCombination);
    if (jsonObj) {
      this.correctCombinations = jsonObj.correctCombinations || [];
    }
  }
}
