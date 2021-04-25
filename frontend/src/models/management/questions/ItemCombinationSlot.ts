export default class ItemCombinationSlot{
    id: number | null = null;
    content: string = '';
    internId!: number;
    column: string = '';
    correctCombination: ItemCombinationSlot[] = [];

    constructor(jsonObj?: Partial<ItemCombinationSlot>) {
        if (jsonObj) {
            Object.assign(this, jsonObj);
            //this.correctCombinations = jsonObj.correctCombinations;
        }
    }

    setAsNew(): void {
        this.id = null;
    }
}