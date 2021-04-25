import StatementQuestionDetails from '@/models/statement/questions/StatementQuestionDetails';
import { QuestionTypes } from '@/services/QuestionHelpers';
import ItemCombinationSlotStatementQuestionDetails from '@/models/statement/questions/ItemCombinationSlotStatementQuestionDetails';

export default class ItemCombinationStatementQuestionDetails extends StatementQuestionDetails{
    items: ItemCombinationSlotStatementQuestionDetails[] = [];

    constructor(jsonObj?: ItemCombinationStatementQuestionDetails) {
      super(QuestionTypes.ItemCombination);
      if (jsonObj) {
          this.items = jsonObj.items || this.items;
            jsonObj.items.map(
                (item: ItemCombinationSlotStatementQuestionDetails) =>
                    new ItemCombinationSlotStatementQuestionDetails(item)
            )
      }
    }
}