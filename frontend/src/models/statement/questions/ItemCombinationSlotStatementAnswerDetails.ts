export default class ItemCombinationSlotStatementAnswerDetails{
    itemId: number | null = null;
    combinationsId: number[] = [];

    constructor(jsonObj?: ItemCombinationSlotStatementAnswerDetails){
        if(jsonObj){
            this.itemId = jsonObj.itemId || this.itemId;
            this.combinationsId = jsonObj.combinationsId || this.combinationsId;
        }
    }
}