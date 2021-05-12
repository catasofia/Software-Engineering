import AnswerDetails from '@/models/management/questions/AnswerDetails';
import { QuestionTypes } from '@/services/QuestionHelpers';
import QuestionDetails from '@/models/management/questions/QuestionDetails';
import OpenAnswerQuestionDetails from '@/models/management/questions/OpenAnswerQuestionDetails';

export default class OpenAnswerAnswerDetails extends AnswerDetails {
  answer!: string;

  constructor(jsonObj?: OpenAnswerAnswerDetails) {
    super(QuestionTypes.OpenAnswer);
    if (jsonObj) {
      this.answer = jsonObj.answer;
    }
  }

  answerRepresentation(question: QuestionDetails): string {
    return '-';
  }

  isCorrect(question: OpenAnswerQuestionDetails): boolean {
    return this.answer === question.suggestion;
  }
}
