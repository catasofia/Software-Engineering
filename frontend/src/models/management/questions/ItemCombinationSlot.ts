export default class ItemCombinationSlot {
  id: number | null = null;
  content: string = '';
  internId!: number;
  column: string = '';
  correctCombinations: ItemCombinationSlot[] = [];

  constructor(jsonObj?: Partial<ItemCombinationSlot>) {
    if (jsonObj) {
      Object.assign(this, jsonObj);
    }
  }

  setAsNew(): void {
    this.id = null;
  }
}
