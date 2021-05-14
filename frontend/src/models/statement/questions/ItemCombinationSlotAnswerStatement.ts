export default class ItemCombinationSlotAnswerStatement {
  id: number | null = null;
  internId: number | null = null;
  correctCombinations: number[] = [];

  constructor(jsonObj?: ItemCombinationSlotAnswerStatement) {
    if (jsonObj) {
      this.id = jsonObj.id || this.id;
      this.internId = jsonObj.internId || this.internId;
      this.correctCombinations = jsonObj.correctCombinations || this.correctCombinations;
    }
  }
}
