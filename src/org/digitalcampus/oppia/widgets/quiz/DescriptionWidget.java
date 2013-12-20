package org.digitalcampus.oppia.widgets.quiz;

import java.util.List;

import org.digitalcampus.mobile.quiz.model.Response;
import org.nurhi.oppia.R;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

public class DescriptionWidget extends QuestionWidget{

	public static final String TAG = DescriptionWidget.class.getSimpleName();
	
	public DescriptionWidget(Activity activity,  View v, ViewGroup container) {
		LinearLayout ll = (LinearLayout) v.findViewById(R.id.quizResponseWidget);
		ll.removeAllViews();
	}
	
	@Override
	public void setQuestionResponses(List<Response> responses, List<String> currentAnswers) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<String> getQuestionResponses(List<Response> responses) {
		// TODO Auto-generated method stub
		return null;
	}

}
