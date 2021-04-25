import AnswerDetails from '@/models/management/questions/AnswerDetails';
import { QuestionTypes, convertToLetter } from '@/services/QuestionHelpers';
import ItemCombinationSlot from '@/models/management/questions/ItemCombinationSlot';
import ItemCombinationQuestionDetails from '@/models/management/questions/ItemCombinationQuestionDetails';

export default class ItemCombinationAnswerDetails extends AnswerDetails {
  items: ItemCombinationSlot[] = [];
  constructor(jsonObj?: ItemCombinationAnswerDetails) {
    super(QuestionTypes.ItemCombination);
    if (jsonObj) {
      this.items = jsonObj.items.map(
        (item: ItemCombinationSlot) => new ItemCombinationSlot(item)
      );
    }
  }

  isCorrect(questionDetails: ItemCombinationQuestionDetails): boolean {
    return false;
  }

  answerRepresentation(questionDetails: ItemCombinationQuestionDetails): string {
    return '';
  }
}
