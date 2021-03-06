package com.GlassHack.glassroom;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.KeyEvent;
import android.widget.TextView;

public class QuestionActivity extends Activity {

	TextView txtview_speechresults;
	DatabaseHandler db;
	String subject;
	String name;
	int correct;
	private static final int SPEECH_REQUEST = 0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_question);
		Intent intent = getIntent();
		subject = (String) intent.getStringExtra("subject");
		db = new DatabaseHandler(this);
		displaySpeechRecognizer();
	}

	private void displaySpeechRecognizer() {
	    Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
	    intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "NAME CORRECT/INCORRECT.");
	    startActivityForResult(intent, SPEECH_REQUEST);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode,
	        Intent data) {
	    if (requestCode == SPEECH_REQUEST && resultCode == RESULT_OK) {
	        List<String> results = data.getStringArrayListExtra(
	                RecognizerIntent.EXTRA_RESULTS);
	        String string = results.get(0);
		    String[] parts = string.split(" ");
		    name = parts[0];
		    String quality = parts[1];
		    if(parts.equals("yes")) {
		    	correct=1;
		    }
		    else {
		    	correct=0;
		    }
		    inputData();
	    }
	    super.onActivityResult(requestCode, resultCode, data);
	}
	
	public void inputData() {
		List<Student> students = db.getAllContacts();
		for(Student cn : students) {
			if(cn.getName().equals(name)) {
				updateStudent(cn);
			}
		}
	}
	
	public void updateStudent(Student cn) {
		if(subject.equals("Math")) {
			cn.setCorrectMath(cn.getCorrectMath()+correct);
		}
		else if(subject.equals("English")) {
			cn.setCorrectEnglish(cn.getCorrectEnglish()+correct);
		}
		else if(subject.equals("Science")) {
			cn.setCorrectScience(cn.getCorrectScience()+correct);
		}
		db.updateContact(cn);
	}
	
	@Override
    public boolean onKeyDown(int keycode, KeyEvent event) {
        if (keycode == KeyEvent.KEYCODE_DPAD_CENTER) {
            Intent intent = new Intent(this,DatabaseViewer.class);
            startActivity(intent);
            return true;
        }
        super.onKeyDown(keycode, event);
        return false;
    }

}
