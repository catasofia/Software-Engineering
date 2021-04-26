export default class ItemCombinationSlotStatementQuestionDetails {
  id: number | null = null;
  content: string = '';
  internId: number | null = null;
  column: string = '';
  correctCombinations: ItemCombinationSlotStatementQuestionDetails[] = [];

  constructor(jsonObj?: ItemCombinationSlotStatementQuestionDetails) {
    if (jsonObj) {
      this.id = jsonObj.id || this.id;
      this.content = jsonObj.content || this.content;
      this.internId = jsonObj.internId || this.internId;
      this.column = jsonObj.column || this.column;
      this.correctCombinations =
        jsonObj.correctCombinations || this.correctCombinations;
    }
  }
}
