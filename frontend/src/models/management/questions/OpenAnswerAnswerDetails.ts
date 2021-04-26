import AnswerDetails from '@/models/management/questions/AnswerDetails';
import { QuestionTypes } from '@/services/QuestionHelpers';
import QuestionDetails from '@/models/management/questions/QuestionDetails';

export default class OpenAnswerAnswerDetails extends AnswerDetails {
  constructor(jsonObj?: OpenAnswerAnswerDetails) {
    super(QuestionTypes.OpenAnswer);
  }

  answerRepresentation(question: QuestionDetails): string {
    return '';
  }

  isCorrect(question: QuestionDetails): boolean {
    return false;
  }
}
