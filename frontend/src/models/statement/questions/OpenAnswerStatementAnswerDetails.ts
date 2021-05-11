import StatementAnswerDetails from '@/models/statement/questions/StatementAnswerDetails';
import { QuestionTypes } from '@/services/QuestionHelpers';

export default class OpenAnswerStatementAnswerDetails extends StatementAnswerDetails {
  public answer!: string;

  constructor(jsonObj?: OpenAnswerStatementAnswerDetails) {
    super(QuestionTypes.OpenAnswer);
    if (jsonObj) {
      this.answer = jsonObj.answer;
    }
  }

  isAnswerCorrect(
    correctAnswerDetails: OpenAnswerStatementAnswerDetails
  ): boolean {
    return (
      !!correctAnswerDetails && this.answer === correctAnswerDetails.answer
    );
  }

  isQuestionAnswered(): boolean {
    return this.answer != null;
  }
}
