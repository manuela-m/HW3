package com.example.hw3;

import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import com.example.hw3.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    Button buttonAdd;
    Button buttonAddBook;
    Button buttonNo;
    Button buttonYes;
    LinearLayout layoutSure;
    LinearLayout layoutEditAdd;
    LinearLayout layoutView;
    TextView sthEmpty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        declaration();
        final FirebaseFirestore db = FirebaseFirestore.getInstance();

        showAllList(db);

        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                setVisibleLayout(layoutEditAdd);
                buttonAdd.setVisibility(View.GONE);

                final Button buttonAddBook = findViewById(R.id.buttonAddBook);
                final EditText editTextTitle = findViewById(R.id.editTextTitle);
                final EditText editTextAuthor = findViewById(R.id.editTextAuthor);
                final EditText editTextYear = findViewById(R.id.editTextYear);
                final EditText editTextType = findViewById(R.id.editTextType);

                buttonAddBook.setText("");
                editTextTitle.setText("");
                editTextAuthor.setText("");
                editTextYear.setText("");
                editTextType.setText("");
                buttonAddBook.setText("Dodaj książke");

                sthEmpty.setVisibility(View.GONE);

                buttonAddBook.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view)
                    {

                        if(editTextTitle.getText().toString().equals("") ||
                                editTextAuthor.getText().toString().equals("") ||
                                editTextYear.getText().toString().equals("") ||
                                editTextType.getText().toString().equals("")) {
                            sthEmpty.setVisibility(View.VISIBLE);
                            return;
                        }

                        buttonAddBook.setClickable(false);

                        Book book = new Book(editTextTitle.getText().toString(), editTextAuthor.getText().toString(),
                                editTextYear.getText().toString(), editTextType.getText().toString());

                        db.collection("books")
                                .add(book)
                                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                    @Override
                                    public void onSuccess(DocumentReference documentReference) {
                                        Log.d("Book", "DocumentSnapshot added with ID: " + documentReference.getId());
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.w("Book", "Error adding document", e);
                                    }
                                });
                        try { Thread.sleep(1000); } catch (InterruptedException e) { e.printStackTrace(); }
                        buttonAddBook.setClickable(true);
                        showAllList(db);
                    }
                });
            }
        });
    }

    private void showAllList(final FirebaseFirestore db) {
        setVisibleLayout(layoutView);
        buttonAdd.setVisibility(View.VISIBLE);
        if(layoutView.getChildCount() > 0)
        {
            layoutView.removeAllViews();
        }
        db.collection("books").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (final QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                        Log.d("Book", document.getId() + " => " + document.getData());

                        TextView textView = new TextView(MainActivity.this);
                        textView.setText("'" + document.get("tytuł") + "', "
                                + document.get("autor") + "\n"
                                + "rok wydania: " + document.get("rokWydania")
                                + ", rodzaj: " + document.get("typ"));

                        textView.setBackgroundColor(Color.parseColor("#FFFFFF"));
                        textView.setTextColor(Color.parseColor("#000000"));
                        textView.setTextSize(18);
                        layoutView.addView(textView);

                        Button buttonEdit = new Button(MainActivity.this);
                        final Button buttonDelete = new Button(MainActivity.this);
                        buttonEdit.setText("Edytuj");
                        buttonDelete.setText("Usuń");
                        buttonEdit.setTextSize(15);
                        buttonDelete.setTextSize(11);
                        buttonEdit.setHeight(22);
                        buttonDelete.setHeight(16);
                        buttonDelete.setTextColor(Color.RED);
                        layoutView.addView(buttonEdit);
                        layoutView.addView(buttonDelete);

                        buttonDelete.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view)
                            {
                                setVisibleLayout(layoutSure);
                                buttonNo.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view)
                                    {
                                        setVisibleLayout(layoutView);
                                    }
                                });
                                buttonYes.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view)
                                    {
                                        buttonYes.setClickable(false);
                                        buttonNo.setClickable(false);
                                        db.collection("books").document(document.getId()).delete()
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        Log.d("Book", "DocumentSnapshot successfully deleted!");
                                                    }
                                                })
                                                .addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        Log.w("Book", "Error deleting document", e);
                                                    }
                                                });
                                        try { Thread.sleep(1000); } catch (InterruptedException e) { e.printStackTrace(); }
                                        buttonYes.setClickable(true);
                                        buttonNo.setClickable(true);
                                        showAllList(db);
                                    }
                                });
                            }
                        });

                        buttonEdit.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view)
                            {
                                setVisibleLayout(layoutEditAdd);
                                buttonAdd.setVisibility(View.GONE);
                                final Button buttonAddBook = findViewById(R.id.buttonAddBook);
                                final EditText editTextTitle = findViewById(R.id.editTextTitle);
                                final EditText editTextAuthor = findViewById(R.id.editTextAuthor);
                                final EditText editTextYear = findViewById(R.id.editTextYear);
                                final EditText editTextType = findViewById(R.id.editTextType);

                                try {
                                    editTextTitle.setText(document.get("tytuł").toString());
                                    editTextAuthor.setText(document.get("autor").toString());
                                    editTextYear.setText(document.get("rokWydania").toString());
                                    editTextType.setText(document.get("typ").toString());
                                }
                                catch (Exception e)
                                {
                                    e.printStackTrace();
                                }
                                buttonAddBook.setText("Zatwierdź zmianę");
                                sthEmpty.setVisibility(View.GONE);
                                buttonAddBook.setOnClickListener(new View.OnClickListener() {

                                    @Override
                                    public void onClick(View view)
                                    {
                                        if(editTextTitle.getText().toString().equals("") ||
                                                editTextAuthor.getText().toString().equals("") ||
                                                editTextYear.getText().toString().equals("") ||
                                                editTextType.getText().toString().equals("")) {
                                            sthEmpty.setVisibility(View.VISIBLE);
                                            return;
                                        }
                                        buttonAddBook.setClickable(false);

                                        db.collection("books").document(document.getId()).delete()
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        Log.d("Book", "DocumentSnapshot successfully deleted!");
                                                    }
                                                })
                                                .addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        Log.w("Book", "Error deleting document", e);
                                                    }
                                                });

                                        Book book = new Book(editTextTitle.getText().toString(), editTextAuthor.getText().toString(),
                                                editTextYear.getText().toString(), editTextType.getText().toString());

                                        db.collection("books")
                                                .add(book)
                                                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                    @Override
                                                    public void onSuccess(DocumentReference documentReference) {
                                                        Log.d("Book", "DocumentSnapshot added with ID: " + documentReference.getId());
                                                    }
                                                })
                                                .addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        Log.w("Book", "Error adding document", e);
                                                    }
                                                });

                                        try { Thread.sleep(1000); } catch (InterruptedException e) { e.printStackTrace(); }
                                        buttonAddBook.setClickable(true);
                                        buttonAdd.setVisibility(View.VISIBLE);
                                        showAllList(db);
                                    }
                                });
                            }

                        });

                    }
                }
            }
        });
    }

    private void declaration() {
        buttonAdd = findViewById(R.id.buttonAdd);
        buttonAddBook = findViewById(R.id.buttonAddBook);
        buttonNo = findViewById(R.id.buttonNo);
        buttonYes = findViewById(R.id.buttonYes);
        layoutSure = findViewById(R.id.layoutSure);
        layoutEditAdd = findViewById(R.id.llayoutEditAdd);
        layoutView = findViewById(R.id.layoutView);
        sthEmpty = findViewById(R.id.textViewSthEmpty);
        buttonAdd = findViewById(R.id.buttonAdd);
    }

    private void setVisibleLayout(LinearLayout layout) {
        layoutView.setVisibility(View.GONE);
        layoutEditAdd.setVisibility(View.GONE);
        layoutSure.setVisibility(View.GONE);
        layout.setVisibility(View.VISIBLE);
    }
}