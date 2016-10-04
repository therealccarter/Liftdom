package com.liftdom.liftdom;

/**
 * Created by Chris on 10/2/2016.
 */

public class MiscOldCode {

    /**
     ArrayList<String> templateListWithNum = new ArrayList<>();
     ArrayList<String> templateNamesListSansNum = new ArrayList<>();

     File myDir = getContext().getFilesDir();
     File[] templateFiles = myDir.listFiles();

     int length = templateFiles.length;


     for(int i = 0; i < length; i++){
     templateListWithNum.add(templateFiles[i].getName());
     }

     for(String withNum : templateListWithNum){
     String sansNum = lastCharRemover(withNum);
     if(!templateNamesListSansNum.contains(sansNum) && !sansNum.equals("instant-ru") && !s3Check(sansNum)){
     templateNamesListSansNum.add(sansNum);
     }

     }

     for(String template : templateNamesListSansNum){

     TemplateListItemFrag templateListItem = new TemplateListItemFrag();

     templateListItem.templateName = template;

     FragmentManager fragmentManager1 = getActivity().getSupportFragmentManager();
     android.support.v4.app.FragmentTransaction fragmentTransaction1 = fragmentManager1.beginTransaction();
     fragmentTransaction1.add(R.id.myTemplatesList, templateListItem);
     fragmentTransaction1.addToBackStack(null);
     fragmentTransaction1.commit();

     }**/


    /**
     mTemplateRef.addValueEventListener(new ValueEventListener() {
    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
    String text = dataSnapshot.getValue(String.class);
    // text view var mConditionTextView.setText(text);
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }
    });

     view.setOnClickListener(new View.OnClickListener() {
     public void onClick(View v) {
     mConditionRef.setValue("Sunny");
     }
     });

     view.setOnClickListener(new View.OnClickListener() {
     public void onClick(View v) {
     mConditionRef.setValue("Foggy");
     }
     });
     **/


}
