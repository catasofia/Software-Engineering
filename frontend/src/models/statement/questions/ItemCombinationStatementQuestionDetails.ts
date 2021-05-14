import StatementQuestionDetails from '@/models/statement/questions/StatementQuestionDetails';
import { QuestionTypes } from '@/services/QuestionHelpers';
import ItemCombinationSlotStatement from '@/models/statement/questions/ItemCombinationSlotStatement';

export default class ItemCombinationStatementQuestionDetails extends StatementQuestionDetails {
  combinations: ItemCombinationSlotStatement[] = [];

  constructor(jsonObj?: ItemCombinationStatementQuestionDetails) {
    super(QuestionTypes.ItemCombination);

    if (jsonObj) {
      this.combinations = jsonObj.combinations
        ? jsonObj.combinations.map(
            (item: ItemCombinationSlotStatement) =>
              new ItemCombinationSlotStatement(item)
          )
        : this.combinations;
    }
  }
}
