import StatementAnswerDetails from '@/models/statement/questions/StatementAnswerDetails';
import { QuestionTypes } from '@/services/QuestionHelpers';

export default class OpenAnswerStatementAnswerDetails extends StatementAnswerDetails {
  constructor(jsonObj?: OpenAnswerStatementAnswerDetails) {
    super(QuestionTypes.OpenAnswer);
  }

  isAnswerCorrect(
    correctAnswerDetails: OpenAnswerStatementAnswerDetails
  ): boolean {
    return true;
  }

  isQuestionAnswered(): boolean {
    return false;
  }
}
