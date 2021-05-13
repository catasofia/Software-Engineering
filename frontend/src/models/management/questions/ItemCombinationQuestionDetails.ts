import QuestionDetails from '@/models/management/questions/QuestionDetails';
import ItemCombinationSlot from '@/models/management/questions/ItemCombinationSlot';
import { QuestionTypes } from '@/services/QuestionHelpers';

export default class ItemCombinationQuestionDetails extends QuestionDetails {
  columnOne: ItemCombinationSlot[] = [new ItemCombinationSlot({internId: 1})];
  columnTwo: ItemCombinationSlot[] = [new ItemCombinationSlot({internId: 2})];

  constructor(jsonObj?: ItemCombinationQuestionDetails) {
    super(QuestionTypes.ItemCombination);
    if (jsonObj) {
      this.columnOne = jsonObj.columnOne
        ? jsonObj.columnOne.map(
            (slot: ItemCombinationSlot) => new ItemCombinationSlot(slot)
          )
        : this.columnOne;

      this.columnTwo = jsonObj.columnTwo
        ? jsonObj.columnTwo.map(
            (slot: ItemCombinationSlot) => new ItemCombinationSlot(slot)
          )
        : this.columnTwo;
    }
  }

  setAsNew(): void {
    this.columnOne.forEach((slot) => {
      slot.setAsNew();
    });

    this.columnTwo.forEach((slot) => {
      slot.setAsNew();
    });
  }
}
