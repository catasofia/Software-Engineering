export default class ItemCombinationSlotStatement {
  combinations: ItemCombinationSlotStatement[] = [];

  constructor(jsonObj?: ItemCombinationSlotStatement) {
    if (jsonObj) {
      this.combinations = jsonObj.combinations || this.combinations;
    }
  }
}
