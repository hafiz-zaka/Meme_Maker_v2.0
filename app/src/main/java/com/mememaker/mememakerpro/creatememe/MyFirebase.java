package com.mememaker.mememakerpro.creatememe;


import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MyFirebase {



    //Initialize Variable

    private FirebaseDatabase firebaseDatabase;
    private String UID;


    public DatabaseReference getDatabaseReference() {
        return databaseReference;
    }

    private DatabaseReference databaseReference;


    //Create private Constructor because not create the object for every class
    private MyFirebase() { }

    //volatile mean that both thread Foreground and Background access this class
    private static volatile MyFirebase INSTANCE =null;

    //Crete Method for creating and accessing the method
    //Working of this Method
    //2 Thread a gy ha at the same time 1st enter ho ga block ma ager object nahe buna class ka wo object bana da ga
    //r return kar da ga fir 2 thread enter ho ga wo daka ga ager object bun gaya ha to wo direct return kar da ga
    public static MyFirebase getInstance()
    {
        if (INSTANCE==null)
        {
            synchronized (MyFirebase.class)
            {
                if (INSTANCE==null)
                {
                    INSTANCE=new MyFirebase();

                    //Firebase Code
                    INSTANCE.firebaseDatabase=FirebaseDatabase.getInstance();
                    INSTANCE.databaseReference=FirebaseDatabase.getInstance().getReference();
                }
            }
        }

        return INSTANCE;
    }



    public FirebaseDatabase getFirebaseDatabase() {
        return firebaseDatabase;
    }

    public String getUID() {
        return UID;
    }


}
