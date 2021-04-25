import StatementAnswerDetails from '@/models/statement/questions/StatementAnswerDetails';
import { QuestionTypes } from '@/services/QuestionHelpers';
import CodeOrderStatementCorrectAnswerDetails from '@/models/statement/questions/CodeOrderStatementCorrectAnswerDetails';
import CodeOrderSlotStatementAnswerDetails from '@/models/statement/questions/CodeOrderSlotStatementAnswerDetails';
import StatementCorrectAnswerDetails from '@/models/statement/questions/StatementCorrectAnswerDetails';

export default class ItemCombinationStatementAnswerDetails extends StatementAnswerDetails {

    constructor(jsonObj?: ItemCombinationStatementAnswerDetails) {
        super(QuestionTypes.ItemCombination);
    }

    isAnswerCorrect(correctAnswerDetails: StatementCorrectAnswerDetails): boolean {
        return false;
    }

    isQuestionAnswered(): boolean {
        return false;
    }
}
