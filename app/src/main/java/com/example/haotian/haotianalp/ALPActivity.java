package com.example.haotian.haotianalp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.Buffer;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;



public class ALPActivity extends Activity implements SensorEventListener {
    protected LockPatternView mPatternView;
    protected PatternGenerator mGenerator;
    protected Button mGenerateButton;
    protected Button mDesigner;
    protected ToggleButton mPracticeToggle;
    protected TextView mSuccessCounterText;

    protected Button mPatternSetButton1;
    protected Button mPatternSetButton2;
    protected Button mPatternSetButton3;
    protected Button mPatternSetButton4;

    private List<Point> mEasterEggPattern;
    protected SharedPreferences mPreferences;
    protected int mGridLength=0;
    protected int mPatternMin=0;
    protected int mPatternMax=0;
    protected String mHighlightMode;
    protected boolean mTactileFeedback;
    protected boolean mRecordingSensorData;

    private static final String TAG = "SensorActivity";
    private static final String TAGmotion = "motionEvent";
    private SensorManager mSensorManager = null;

    public List<Sensor> deviceSensors;
    private  Sensor mAccelerometer, mMagnetometer, mGyroscope, mRotation, mGravity, mLinearAcc;

    private File file;
    public static String[] mLine;
    public BufferedWriter bufferedWriter;
    private VelocityTracker mVelocityTracker = null;
    private int control = 0;
    DateFormat mDateFormat;
    String mTimestamp;
    private int counter=0;
    private int successes = 0;  // Counters!
    private String myStr = "";

    // these are lists used to save the touch information
    // ------- Part 4
    private List<Long> timeStampBuffer = new ArrayList<>();
    private List<Integer> counterBuffer = new ArrayList<>();
    private List<Integer> counterList = new ArrayList<>();

    private List<Float> xAccelerometerBuffer = new ArrayList<>();
    private List<Float> yAccelerometerBuffer = new ArrayList<>();
    private List<Float> zAccelerometerBuffer = new ArrayList<>();
    private List<Float> xMagneticBuffer = new ArrayList<>();
    private List<Float> yMagneticBuffer = new ArrayList<>();
    private List<Float> zMagneticBuffer = new ArrayList<>();
    private List<Float> xGyroscopeBuffer = new ArrayList<>();
    private List<Float> yGyroscopeBuffer = new ArrayList<>();
    private List<Float> zGyroscopeBuffer = new ArrayList<>();
    private List<Float> xRotationBuffer = new ArrayList<>();
    private List<Float> yRotationBuffer = new ArrayList<>();
    private List<Float> zRotationBuffer = new ArrayList<>();
    private List<Float> xLinearAccelBuffer = new ArrayList<>();
    private List<Float> yLinearAccelBuffer = new ArrayList<>();
    private List<Float> zLinearAccelBuffer = new ArrayList<>();
    private List<Float> xGravityBuffer = new ArrayList<>();
    private List<Float> yGravityBuffer = new ArrayList<>();
    private List<Float> zGravityBuffer = new ArrayList<>();
    private List<Float> xPositionsBuffer = new ArrayList<>();
    private List<Float> yPositionsBuffer = new ArrayList<>();
    private List<Float> xVelocityBuffer = new ArrayList<>();
    private List<Float> yVelocityBuffer = new ArrayList<>();
    private List<Float> pressureBuffer = new ArrayList<>();
    private List<Float> sizeBuffer = new ArrayList<>();

    // ------- Part 3
    public List<Long> timeStampList = new ArrayList<>();
    public List<Float> xAccelerometerList = new ArrayList<>();
    public List<Float> yAccelerometerList = new ArrayList<>();
    public List<Float> zAccelerometerList = new ArrayList<>();
    public List<Float> xMagneticList = new ArrayList<>();
    public List<Float> yMagneticList = new ArrayList<>();
    public List<Float> zMagneticList = new ArrayList<>();
    public List<Float> xGyroscopeList = new ArrayList<>();
    public List<Float> yGyroscopeList = new ArrayList<>();
    public List<Float> zGyroscopeList = new ArrayList<>();
    public List<Float> xRotationList = new ArrayList<>();
    public List<Float> yRotationList = new ArrayList<>();
    public List<Float> zRotationList = new ArrayList<>();
    public List<Float> xLinearAccelList = new ArrayList<>();
    public List<Float> yLinearAccelList = new ArrayList<>();
    public List<Float> zLinearAccelList = new ArrayList<>();
    public List<Float> xGravityList = new ArrayList<>();
    public List<Float> yGravityList = new ArrayList<>();
    public List<Float> zGravityList = new ArrayList<>();
    // ------- Part 2
    public List<Float> xPositionsList = new ArrayList<>();
    public List<Float> yPositionsList = new ArrayList<>();
    public List<Float> pressureList = new ArrayList<>();
    public List<Float> sizeList = new ArrayList<>();
    public List<Float> xVelocityList = new ArrayList<>();
    public List<Float> yVelocityList = new ArrayList<>();

    //Assignment two lists for Mean and Deviation
    public List<Float> xAccelerometer_MEAN = new ArrayList<>();
    public List<Float> yAccelerometer_MEAN = new ArrayList<>();
    public List<Float> zAccelerometer_MEAN = new ArrayList<>();
    public List<Float> xMagnetic_MEAN = new ArrayList<>();
    public List<Float> yMagnetic_MEAN = new ArrayList<>();
    public List<Float> zMagnetic_MEAN = new ArrayList<>();
    public List<Float> xGyroscope_MEAN = new ArrayList<>();
    public List<Float> yGyroscope_MEAN = new ArrayList<>();
    public List<Float> zGyroscope_MEAN = new ArrayList<>();
    public List<Float> xRotation_MEAN = new ArrayList<>();
    public List<Float> yRotation_MEAN = new ArrayList<>();
    public List<Float> zRotation_MEAN = new ArrayList<>();
    public List<Float> xLinAccel_MEAN = new ArrayList<>();
    public List<Float> yLinAccel_MEAN = new ArrayList<>();
    public List<Float> zLinAccel_MEAN = new ArrayList<>();
    public List<Float> xGrav_MEAN = new ArrayList<>();
    public List<Float> yGrav_MEAN = new ArrayList<>();
    public List<Float> zGrav_MEAN = new ArrayList<>();
    public List<Float> pressure_MEAN = new ArrayList<>();
    public List<Float> size_MEAN = new ArrayList<>();
    public List<Float> xVelocity_MEAN = new ArrayList<>();
    public List<Float> yVelocity_MEAN = new ArrayList<>();
    /////////////////////////////////////////////////////
    public List<Float> xAccelerometer_STDEV = new ArrayList<>();
    public List<Float> yAccelerometer_STDEV = new ArrayList<>();
    public List<Float> zAccelerometer_STDEV = new ArrayList<>();
    public List<Float> xMagnetic_STDEV = new ArrayList<>();
    public List<Float> yMagnetic_STDEV = new ArrayList<>();
    public List<Float> zMagnetic_STDEV = new ArrayList<>();
    public List<Float> xGyroscope_STDEV = new ArrayList<>();
    public List<Float> yGyroscope_STDEV = new ArrayList<>();
    public List<Float> zGyroscope_STDEV = new ArrayList<>();
    public List<Float> xRotation_STDEV = new ArrayList<>();
    public List<Float> yRotation_STDEV = new ArrayList<>();
    public List<Float> zRotation_STDEV = new ArrayList<>();
    public List<Float> xLinAccel_STDEV = new ArrayList<>();
    public List<Float> yLinAccel_STDEV = new ArrayList<>();
    public List<Float> zLinAccel_STDEV = new ArrayList<>();
    public List<Float> xGrav_STDEV = new ArrayList<>();
    public List<Float> yGrav_STDEV = new ArrayList<>();
    public List<Float> zGrav_STDEV = new ArrayList<>();
    public List<Float> pressure_STDEV = new ArrayList<>();
    public List<Float> size_STDEV = new ArrayList<>();
    public List<Float> xVelocity_STDEV = new ArrayList<>();
    public List<Float> yVelocity_STDEV = new ArrayList<>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        mGenerator = new PatternGenerator();

        setContentView(R.layout.activity_alp);
        mPatternView = (LockPatternView) findViewById(R.id.pattern_view);
        mSuccessCounterText = (TextView) findViewById(R.id.CorrectText);

        // ADJUST GENERATE PATTERN BUTTON
        mGenerateButton = (Button) findViewById(R.id.generate_button);
        mGenerateButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Log.i("click","'Randomly Generate Pattern' button was pressed.");
                        mPatternView.setPattern(mGenerator.getPattern());
                        mPracticeToggle.setEnabled(true);
                        mPatternView.invalidate();
                    }
                }
        );

        // ADJUST PRACTICE BUTTON
        mPracticeToggle = (ToggleButton) findViewById(R.id.practice_toggle);
        mPracticeToggle.setEnabled(false);
        mPracticeToggle.setOnCheckedChangeListener(
                new ToggleButton.OnCheckedChangeListener() {
                    public void onCheckedChanged(CompoundButton buttonView,
                                                 boolean isChecked) {

                        mGenerateButton.setEnabled(!isChecked);
                        if (!isChecked) {
                            Log.i("SAVEDATA", "Toggled back to false, save the data in the lists");
                            //writeDataToCSV();
                            writeDataToCSV_MeanAndDev();
                            mPatternView.setPracticeMode(false);
                            //TODO:clear data
                        }
                        else {
                            mPatternView.setPracticeMode(true);
                            Log.i("RECORDINGDATA","Toggled to true, recording the data");
                        }
                    }
                });

        mPatternSetButton1 = (Button) findViewById(R.id.pattern1_button);
        mPatternSetButton1.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        List<Point> tempPattern = new ArrayList();
                        tempPattern.add(new Point(0, 1));
                        tempPattern.add(new Point(1, 2));
                        tempPattern.add(new Point(2, 1));
                        tempPattern.add(new Point(1, 0));
                        mPatternView.setPattern(tempPattern);
                        mPracticeToggle.setEnabled(true);
                        mPatternView.invalidate();
                        successes = 0;
                    }
                });
        mPatternSetButton2 = (Button) findViewById(R.id.pattern2_button);
        mPatternSetButton2.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view){
                        List<Point> tempPattern = new ArrayList();
                        tempPattern.add(new Point(0, 0));
                        tempPattern.add(new Point(1, 0));
                        tempPattern.add(new Point(2, 0));
                        tempPattern.add(new Point(1, 1));
                        tempPattern.add(new Point(0, 2));
                        mPatternView.setPattern(tempPattern);
                        mPracticeToggle.setEnabled(true);
                        mPatternView.invalidate();
                        successes = 0;
                    }
                });
        mPatternSetButton3 = (Button) findViewById(R.id.pattern3_button);
        mPatternSetButton3.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        List<Point> tempPattern = new ArrayList();
                        tempPattern.add(new Point(0, 1));
                        tempPattern.add(new Point(1, 1));
                        tempPattern.add(new Point(2, 1));
                        tempPattern.add(new Point(2, 0));
                        tempPattern.add(new Point(1, 0));
                        tempPattern.add(new Point(0, 0));
                        mPatternView.setPattern(tempPattern);
                        mPracticeToggle.setEnabled(true);
                        mPatternView.invalidate();
                        successes = 0;
                    }
                });
        mPatternSetButton4 = (Button) findViewById(R.id.pattern4_button);
        mPatternSetButton4.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        List<Point> tempPattern = new ArrayList();
                        tempPattern.add(new Point(1, 0));
                        tempPattern.add(new Point(1, 1));
                        tempPattern.add(new Point(0, 2));
                        tempPattern.add(new Point(0, 1));
                        mPatternView.setPattern(tempPattern);
                        mPracticeToggle.setEnabled(true);
                        mPatternView.invalidate();
                        successes = 0;
                    }
                });

        // bind up manager
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        // bind up sensors
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mMagnetometer = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        mGyroscope = mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        mRotation = mSensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);
        mLinearAcc = mSensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
        mGravity = mSensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY);
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        mSensorManager.registerListener(this, mAccelerometer, mSensorManager.SENSOR_DELAY_FASTEST);
        mSensorManager.registerListener(this, mMagnetometer, mSensorManager.SENSOR_DELAY_FASTEST);
        mSensorManager.registerListener(this, mGyroscope, mSensorManager.SENSOR_DELAY_FASTEST);
        mSensorManager.registerListener(this, mRotation, mSensorManager.SENSOR_DELAY_FASTEST);
        mSensorManager.registerListener(this, mLinearAcc, mSensorManager.SENSOR_DELAY_FASTEST);
        mSensorManager.registerListener(this, mGravity, mSensorManager.SENSOR_DELAY_FASTEST);

        updateFromPrefs();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_al, menu);
        return true;
    }

    @Override
    protected void onPause() {
        // turn off the sensors to save battery
        super.onPause();
        mSensorManager.unregisterListener(this);
    }
    @Override
    public final void onAccuracyChanged(Sensor sensor, int accuracy){}

    @Override
    public final void onSensorChanged(SensorEvent event){
        if (mRecordingSensorData){
            timeStampBuffer.add(event.timestamp);
            if (event.sensor == mAccelerometer){
                xAccelerometerBuffer.add(event.values[0]);
                yAccelerometerBuffer.add(event.values[1]);
                zAccelerometerBuffer.add(event.values[2]);
            }
            if (event.sensor == mMagnetometer){
                xMagneticBuffer.add(event.values[0]);
                yMagneticBuffer.add(event.values[1]);
                zMagneticBuffer.add(event.values[2]);
            }
            if (event.sensor == mGyroscope){
                yGyroscopeBuffer.add(event.values[0]);
                xGyroscopeBuffer.add(event.values[1]);
                zGyroscopeBuffer.add(event.values[2]);
            }
            if (event.sensor == mRotation){
                yRotationBuffer.add(event.values[0]);
                zRotationBuffer.add(event.values[1]);
                xRotationBuffer.add(event.values[2]);
            }
            if (event.sensor == mLinearAcc){
                xLinearAccelBuffer.add(event.values[0]);
                yLinearAccelBuffer.add(event.values[1]);
                zLinearAccelBuffer.add(event.values[2]);
            }
            if (event.sensor == mGravity){
                xGravityBuffer.add(event.values[0]);
                yGravityBuffer.add(event.values[1]);
                zGravityBuffer.add(event.values[2]);
            }
        }
    }

    @Override    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void writeDataToCSV()
    {
        try {
            //create the file
            String filename = "MeanStdevDatadump.csv";
            File file = new File(Environment.getExternalStorageDirectory(), filename);
            file.createNewFile();

            // Create the writing stream
            FileWriter fw = new FileWriter(file.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);

            StringBuilder outputString = new StringBuilder();
            //loop through all the data we want to write for each column
            // do the header first
            outputString.append("TimeStamp, " +
                    "TYPE_ACCELEROMETER_X, TYPE_ACCELEROMETER_Y, TYPE_ACCELEROMETER_Z, " +
                    "TYPE_MAGNETIC_FIELD_X, TYPE_MAGNETIC_FIELD_Y, TYPE_MAGNETIC_FIELD_Z, " +
                    "TYPE_GYROSCOPE_X, TYPE_GYROSCOPE_Y, TYPE_GYROSCOPE_Z, " +
                    "TYPE_ROTATION_VECTOR_X, TYPE_ROTATION_VECTOR_Y, TYPE_ROTATION_VECTOR_Z, " +
                    "TYPE_LINEAR_ACCELERATION_X, TYPE_LINEAR_ACCELERATION_Y, TYPE_LINEAR_ACCELERATION_Z, " +
                    "TYPE_GRAVITY_X, TYPE_GRAVITY_Y, TYPE_GRAVITY_Z, " +
                    "position_X, position_Y, velocity_X, velocity_Y, pressure, size, " +
                    "mCurrentPattern, Counter, \n");

            for(int i = 0; i < counterList.size(); i++){
                if (i > timeStampList.size() - 1) {
                    outputString.append(timeStampList.get(timeStampList.size()-1) + ", ");
                }else {
                    outputString.append(timeStampList.get(i) + ", ");
                }
                // this is the sensors
                if (i > xAccelerometerList.size() - 1) {
                    outputString.append(xAccelerometerList.get(xAccelerometerList.size()-1) + ", ");
                } else {
                    outputString.append(xAccelerometerList.get(i) + ", ");
                }
                if (i > yAccelerometerList.size() - 1) {
                    outputString.append(yAccelerometerList.get(yAccelerometerList.size()-1) + ", ");
                } else {
                    outputString.append(yAccelerometerList.get(i) + ", ");
                }
                if (i > zAccelerometerList.size() - 1) {
                    outputString.append(zAccelerometerList.get(zAccelerometerList.size()-1) + ", ");
                } else {
                    outputString.append(zAccelerometerList.get(i) + ", ");
                }
                if (i > xMagneticList.size() - 1) {
                    outputString.append(xMagneticList.get(xMagneticList.size()-1) + ", ");
                } else {
                    outputString.append(xMagneticList.get(i) + ", ");
                }
                if (i > yMagneticList.size() - 1) {
                    outputString.append(yMagneticList.get(yMagneticList.size()-1) + ", ");
                } else {
                    outputString.append(yMagneticList.get(i) + ", ");
                }
                if (i > zMagneticList.size() - 1) {
                    outputString.append(zMagneticList.get(zMagneticList.size()-1) + ", ");
                } else {
                    outputString.append(zMagneticList.get(i) + ", ");
                }if (i > xGyroscopeList.size() - 1) {
                    outputString.append(xGyroscopeList.get(xGyroscopeList.size()-1) + ", ");
                } else {
                    outputString.append(xGyroscopeList.get(i) + ", ");}
                if (i > yGyroscopeList.size() - 1) {
                    outputString.append(yGyroscopeList.get(yGyroscopeList.size()-1) + ", ");
                } else {
                    outputString.append(yGyroscopeList.get(i) + ", ");
                }
                if (i > zGyroscopeList.size() - 1) {
                    outputString.append(zGyroscopeList.get(zGyroscopeList.size()-1) + ", ");
                } else {
                    outputString.append(zGyroscopeList.get(i) + ", ");
                }
                if (i > xRotationList.size() - 1) {
                    outputString.append(xRotationList.get(xRotationList.size()-1) + ", ");
                } else {
                    outputString.append(xRotationList.get(i) + ", ");
                }
                if (i > yRotationList.size() - 1) {
                    outputString.append(yRotationList.get(yRotationList.size()-1) + ", ");
                } else {
                    outputString.append(yRotationList.get(i) + ", ");
                }
                if (i > zRotationList.size() - 1) {
                    outputString.append(zRotationList.get(zRotationList.size()-1) + ", ");
                } else {
                    outputString.append(zRotationList.get(i) + ", ");
                }
                if (i > xLinearAccelList.size() - 1) {
                    outputString.append(xLinearAccelList.get(xLinearAccelList.size()-1) + ", ");
                } else {
                    outputString.append(xLinearAccelList.get(i) + ", ");
                }
                if (i > yLinearAccelList.size() - 1) {
                    outputString.append(yLinearAccelList.get(yLinearAccelList.size()-1) + ", ");
                } else {
                    outputString.append(yLinearAccelList.get(i) + ", ");
                }
                if (i > zLinearAccelList.size() - 1) {
                    outputString.append(zLinearAccelList.get(zLinearAccelList.size()-1) + ", ");
                } else {
                    outputString.append(zLinearAccelList.get(i) + ", ");
                }
                if (i > xGravityList.size() - 1) {
                    outputString.append(xGravityList.get(xGravityList.size()-1) + ", ");
                } else {
                    outputString.append(xGravityList.get(i) + ", ");
                }
                if (i > yGravityList.size() - 1) {
                    outputString.append(yGravityList.get(yGravityList.size()-1) + ", ");
                } else {
                    outputString.append(yGravityList.get(i) + ", ");
                }
                if (i > zGravityList.size() - 1) {
                    outputString.append(zGravityList.get(zGravityList.size()-1) + ", ");
                } else {
                    outputString.append(zGravityList.get(i) + ", ");
                }
                // X Position
                if (i > xPositionsList.size()-1){
                    outputString.append(xPositionsList.get(xPositionsList.size()-1) + ", ");
                } else {
                    outputString.append(xPositionsList.get(i) + ", ");
                }
                // Y Position
                if (i > yPositionsList.size()-1){
                    outputString.append(yPositionsList.get(yPositionsList.size()-1) + ", ");
                } else {
                    outputString.append(yPositionsList.get(i) + ", ");
                }
                // X Velocity
                if (i > xVelocityList.size()-1){
                    outputString.append(xVelocityList.get(xVelocityList.size()-1) + ", ");
                } else {
                    outputString.append(xVelocityList.get(i) + ", ");
                }
                // Y Velocity
                if (i > yVelocityList.size()-1){
                    outputString.append(yVelocityList.get(yVelocityList.size()-1) + ", ");
                } else {
                    outputString.append(yVelocityList.get(i) + ", ");
                }
                // Pressure
                if (i > pressureList.size()-1){
                    outputString.append(pressureList.get(pressureList.size()-1) + ", ");
                } else {
                    outputString.append(pressureList.get(i) + ", ");
                }
                // Size
                if (i > sizeList.size()-1) {
                    outputString.append(sizeList.get(sizeList.size()-1) + ", ");
                } else {
                    outputString.append(sizeList.get(i) + ", ");
                }

                // Current pattern string
                outputString.append(mPatternView.mCurrentPattern.toString().replace(',',' ') + ", ");

                // Counter value
                if (i > counterList.size()-1) {
                    outputString.append(counter + ", ");
                }
                else {
                    outputString.append(counterList.get(i) + ", ");
                }

                // line break
                outputString.append("\n");
            }

            // do the actual writing to file
            Log.i("WRITING", "Writing the data to file");
            bw.write(outputString.toString());
            // Log.i("CLOSING", "Closing the file that was written at " + Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).toString() + " /" + filename);
            bw.close();

            // scan the new media so the device knows it's there and it can be accessed
            MediaScannerConnection.scanFile(this,
                    new String[]{file.toString()}, null,
                    new MediaScannerConnection.OnScanCompletedListener() {
                        public void onScanCompleted(String path, Uri uri) {
                            Log.i("ExternalStorage", "Scanned " + path + ":");
                            Log.i("ExternalStorage", "-> uri=" + uri);
                        }
                    });

        } catch (IOException ex){
            Log.e("IOEXCEPTION", "There was a problem either finding the file, or writing");
            Log.e("ERRORMSG",ex.getMessage());
            Log.e("ERRORSTK",ex.getStackTrace().toString());
        }
    }

    private void writeDataToCSV_MeanAndDev()
    {
        try {
            //create the file
            String filename = "MeanStdevDatadump.csv";
            File file = new File(Environment.getExternalStorageDirectory(), filename);
            file.createNewFile();

            // Create the writing stream
            FileWriter fw = new FileWriter(file.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);

            StringBuilder outputString = new StringBuilder();
            //loop through all the data we want to write for each column
            // do the header first
            outputString.append(
                    "ACCELEROMETER_X_MEAN, ACCELEROMETER_X_STDEV," +
                            "ACCELEROMETER_Y_MEAN, ACCELEROMETER_Y_STDEV," +
                                "ACCELEROMETER_Z_MEAN, ACCELEROMETER_Z_STDEV," +
                    "MAGNETIC_FIELD_X_MEAN, MAGNETIC_FIELD_X_STDEV, " +
                            "MAGNETIC_FIELD_Y_MEAN, MAGNETIC_FIELD_Y_STDEV, " +
                                "MAGNETIC_FIELD_Z_MEAN, MAGNETIC_FIELD_Z_STDEV, " +
                    "GYROSCOPE_X_MEAN, GYROSCOPE_X_STDEV, " +
                            "GYROSCOPE_Y_MEAN, GYROSCOPE_Y_STDEV, " +
                                "GYROSCOPE_Z_MEAN, GYROSCOPE_Z_STDEV, " +
                    "ROTATION_VECTOR_X_MEAN, ROTATION_VECTOR_X_STDEV, " +
                            "ROTATION_VECTOR_Y_MEAN, ROTATION_VECTOR_Y_STDEV, " +
                                "ROTATION_VECTOR_Z_MEAN, ROTATION_VECTOR_Z_STDEV, " +
                    "LINEAR_ACCELERATION_X_MEAN, LINEAR_ACCELERATION_X_STDEV, " +
                            "LINEAR_ACCELERATION_Y_MEAN, LINEAR_ACCELERATION_X_STDEV, " +
                                "LINEAR_ACCELERATION_Z_MEAN, LINEAR_ACCELERATION_X_STDEV, " +
                    "GRAVITY_X_MEAN, GRAVITY_X_STDEV, " +
                            "GRAVITY_Y_MEAN, GRAVITY_Y_STDEV, " +
                                "GRAVITY_Z_MEAN, GRAVITY_Z_STDEV, " +
                    "velocity_X_MEAN, velocity_X_STDEV, velocity_Y_MEAN, velocity_Y_STDEV, " +
                            "pressure_MEAN, pressure_STDEV, size_MEAN, size_STDEV, " +
                    "mCurrentPattern, Counter, \n");

            for(int i = 0; i < successes; i++){
                // Accelerometers
                if (i > xAccelerometer_MEAN.size() - 1) {
                    outputString.append(xAccelerometer_MEAN.get(xAccelerometer_MEAN.size()-1) + ", ");
                } else {
                    outputString.append(xAccelerometer_MEAN.get(i) + ", ");
                }
                if (i > xAccelerometer_STDEV.size() - 1) {
                    outputString.append(xAccelerometer_STDEV.get(xAccelerometer_STDEV.size()-1) + ", ");
                } else {
                    outputString.append(xAccelerometer_STDEV.get(i) + ", ");
                }
                if (i > yAccelerometer_MEAN.size() - 1) {
                    outputString.append(yAccelerometer_MEAN.get(yAccelerometer_MEAN.size()-1) + ", ");
                } else {
                    outputString.append(yAccelerometer_MEAN.get(i) + ", ");
                }
                if (i > yAccelerometer_STDEV.size() - 1) {
                    outputString.append(yAccelerometer_STDEV.get(yAccelerometer_STDEV.size()-1) + ", ");
                } else {
                    outputString.append(yAccelerometer_STDEV.get(i) + ", ");
                }
                if (i > zAccelerometer_MEAN.size() - 1) {
                    outputString.append(zAccelerometer_MEAN.get(zAccelerometer_MEAN.size()-1) + ", ");
                } else {
                    outputString.append(zAccelerometer_MEAN.get(i) + ", ");
                }
                if (i > zAccelerometer_STDEV.size() - 1) {
                    outputString.append(zAccelerometer_STDEV.get(zAccelerometer_STDEV.size()-1) + ", ");
                } else {
                    outputString.append(zAccelerometer_STDEV.get(i) + ", ");
                }
                // Magnetic Field
                if (i > xGrav_MEAN.size() - 1) {
                    outputString.append(xGrav_MEAN.get(xGrav_MEAN.size()-1) + ", ");
                } else {
                    outputString.append(xGrav_MEAN.get(i) + ", ");
                }
                if (i > xGrav_STDEV.size() - 1) {
                    outputString.append(xGrav_STDEV.get(xGrav_STDEV.size()-1) + ", ");
                } else {
                    outputString.append(xGrav_STDEV.get(i) + ", ");
                }
                if (i > yGrav_MEAN.size() - 1) {
                    outputString.append(yGrav_MEAN.get(yGrav_MEAN.size()-1) + ", ");
                } else {
                    outputString.append(yGrav_MEAN.get(i) + ", ");
                }
                if (i > yGrav_STDEV.size() - 1) {
                    outputString.append(yGrav_STDEV.get(yGrav_STDEV.size()-1) + ", ");
                } else {
                    outputString.append(yGrav_STDEV.get(i) + ", ");
                }
                if (i > zGrav_MEAN.size() - 1) {
                    outputString.append(zGrav_MEAN.get(zGrav_MEAN.size()-1) + ", ");
                } else {
                    outputString.append(zGrav_MEAN.get(i) + ", ");
                }
                if (i > zGrav_STDEV.size() - 1) {
                    outputString.append(zGrav_STDEV.get(zGrav_STDEV.size()-1) + ", ");
                } else {
                    outputString.append(zGrav_STDEV.get(i) + ", ");
                }
                // Gyroscope
                if (i > xGyroscope_MEAN.size() - 1) {
                    outputString.append(xGyroscope_MEAN.get(xGyroscope_MEAN.size()-1) + ", ");
                } else {
                    outputString.append(xGyroscope_MEAN.get(i) + ", ");
                }
                if (i > xGyroscope_STDEV.size() - 1) {
                    outputString.append(xGyroscope_STDEV.get(xGyroscope_STDEV.size()-1) + ", ");
                } else {
                    outputString.append(xGyroscope_STDEV.get(i) + ", ");
                }
                if (i > yGyroscope_MEAN.size() - 1) {
                    outputString.append(yGyroscope_MEAN.get(yGyroscope_MEAN.size()-1) + ", ");
                } else {
                    outputString.append(yGyroscope_MEAN.get(i) + ", ");
                }
                if (i > yGyroscope_STDEV.size() - 1) {
                    outputString.append(yGyroscope_STDEV.get(yGyroscope_STDEV.size()-1) + ", ");
                } else {
                    outputString.append(yGyroscope_STDEV.get(i) + ", ");
                }
                if (i > zGyroscope_MEAN.size() - 1) {
                    outputString.append(zGyroscope_MEAN.get(zGyroscope_MEAN.size()-1) + ", ");
                } else {
                    outputString.append(zGyroscope_MEAN.get(i) + ", ");
                }
                if (i > zGyroscope_STDEV.size() - 1) {
                    outputString.append(zGyroscope_STDEV.get(zGyroscope_STDEV.size()-1) + ", ");
                } else {
                    outputString.append(zGyroscope_STDEV.get(i) + ", ");
                }
                // Rotation
                if (i > xRotation_MEAN.size() - 1) {
                    outputString.append(xRotation_MEAN.get(xRotation_MEAN.size()-1) + ", ");
                } else {
                    outputString.append(xRotation_MEAN.get(i) + ", ");
                }
                if (i > xRotation_STDEV.size() - 1) {
                    outputString.append(xRotation_STDEV.get(xRotation_STDEV.size()-1) + ", ");
                } else {
                    outputString.append(xRotation_STDEV.get(i) + ", ");
                }
                if (i > yRotation_MEAN.size() - 1) {
                    outputString.append(yRotation_MEAN.get(yRotation_MEAN.size()-1) + ", ");
                } else {
                    outputString.append(yRotation_MEAN.get(i) + ", ");
                }
                if (i > yRotation_STDEV.size() - 1) {
                    outputString.append(yRotation_STDEV.get(yRotation_STDEV.size()-1) + ", ");
                } else {
                    outputString.append(yRotation_STDEV.get(i) + ", ");
                }
                if (i > zRotation_MEAN.size() - 1) {
                    outputString.append(zRotation_MEAN.get(zRotation_MEAN.size()-1) + ", ");
                } else {
                    outputString.append(zRotation_MEAN.get(i) + ", ");
                }
                if (i > zRotation_STDEV.size() - 1) {
                    outputString.append(zRotation_STDEV.get(zRotation_STDEV.size()-1) + ", ");
                } else {
                    outputString.append(zRotation_STDEV.get(i) + ", ");
                }
                // Linear Acceleration
                if (i > xLinAccel_MEAN.size() - 1) {
                    outputString.append(xLinAccel_MEAN.get(xLinAccel_MEAN.size()-1) + ", ");
                } else {
                    outputString.append(xLinAccel_MEAN.get(i) + ", ");
                }
                if (i > xLinAccel_STDEV.size() - 1) {
                    outputString.append(xLinAccel_STDEV.get(xLinAccel_STDEV.size()-1) + ", ");
                } else {
                    outputString.append(xLinAccel_STDEV.get(i) + ", ");
                }
                if (i > yLinAccel_MEAN.size() - 1) {
                    outputString.append(yLinAccel_MEAN.get(yLinAccel_MEAN.size()-1) + ", ");
                } else {
                    outputString.append(yLinAccel_MEAN.get(i) + ", ");
                }
                if (i > yLinAccel_STDEV.size() - 1) {
                    outputString.append(yLinAccel_STDEV.get(yLinAccel_STDEV.size()-1) + ", ");
                } else {
                    outputString.append(yLinAccel_STDEV.get(i) + ", ");
                }
                if (i > zLinAccel_MEAN.size() - 1) {
                    outputString.append(zLinAccel_MEAN.get(zLinAccel_MEAN.size()-1) + ", ");
                } else {
                    outputString.append(zLinAccel_MEAN.get(i) + ", ");
                }
                if (i > zLinAccel_STDEV.size() - 1) {
                    outputString.append(zLinAccel_STDEV.get(zLinAccel_STDEV.size()-1) + ", ");
                } else {
                    outputString.append(zLinAccel_STDEV.get(i) + ", ");
                }
                // Gravity
                if (i > xGrav_MEAN.size() - 1) {
                    outputString.append(xGrav_MEAN.get(xGrav_MEAN.size()-1) + ", ");
                } else {
                    outputString.append(xGrav_MEAN.get(i) + ", ");
                }
                if (i > xGrav_STDEV.size() - 1) {
                    outputString.append(xGrav_STDEV.get(xGrav_STDEV.size()-1) + ", ");
                } else {
                    outputString.append(xGrav_STDEV.get(i) + ", ");
                }
                if (i > yGrav_MEAN.size() - 1) {
                    outputString.append(yGrav_MEAN.get(yGrav_MEAN.size()-1) + ", ");
                } else {
                    outputString.append(yGrav_MEAN.get(i) + ", ");
                }
                if (i > yGrav_STDEV.size() - 1) {
                    outputString.append(yGrav_STDEV.get(yGrav_STDEV.size()-1) + ", ");
                } else {
                    outputString.append(yGrav_STDEV.get(i) + ", ");
                }
                if (i > zGrav_MEAN.size() - 1) {
                    outputString.append(zGrav_MEAN.get(zGrav_MEAN.size()-1) + ", ");
                } else {
                    outputString.append(zGrav_MEAN.get(i) + ", ");
                }
                if (i > zGrav_STDEV.size() - 1) {
                    outputString.append(zGrav_STDEV.get(zGrav_STDEV.size()-1) + ", ");
                } else {
                    outputString.append(zGrav_STDEV.get(i) + ", ");
                }
                // X Velocity
                if (i > xVelocity_MEAN.size()-1){
                    outputString.append(xVelocity_MEAN.get(xVelocity_MEAN.size()-1) + ", ");
                } else {
                    outputString.append(xVelocity_MEAN.get(i) + ", ");
                }
                if (i > xVelocity_STDEV.size()-1){
                    outputString.append(xVelocity_STDEV.get(xVelocity_STDEV.size()-1) + ", ");
                } else {
                    outputString.append(xVelocity_STDEV.get(i) + ", ");
                }
                // Y Velocity
                if (i > yVelocity_MEAN.size()-1){
                    outputString.append(yVelocity_MEAN.get(yVelocity_MEAN.size()-1) + ", ");
                } else {
                    outputString.append(yVelocity_MEAN.get(i) + ", ");
                }
                if (i > yVelocity_STDEV.size()-1){
                    outputString.append(yVelocity_STDEV.get(yVelocity_STDEV.size()-1) + ", ");
                } else {
                    outputString.append(yVelocity_STDEV.get(i) + ", ");
                }
                // Pressure
                if (i > pressure_MEAN.size()-1){
                    outputString.append(pressure_MEAN.get(pressure_MEAN.size()-1) + ", ");
                } else {
                    outputString.append(pressure_MEAN.get(i) + ", ");
                }
                if (i > pressure_STDEV.size()-1){
                    outputString.append(pressure_STDEV.get(pressure_STDEV.size()-1) + ", ");
                } else {
                    outputString.append(pressure_STDEV.get(i) + ", ");
                }
                // Size
                if (i > size_MEAN.size()-1) {
                    outputString.append(size_MEAN.get(size_MEAN.size()-1) + ", ");
                } else {
                    outputString.append(size_MEAN.get(i) + ", ");
                }
                if (i > size_STDEV.size()-1) {
                    outputString.append(size_STDEV.get(size_STDEV.size()-1) + ", ");
                } else {
                    outputString.append(size_STDEV.get(i) + ", ");
                }

                // Current pattern string
                outputString.append(mPatternView.mCurrentPattern.toString().replace(',',' ') + ", ");

                // Counter value
                if (i > counterList.size()-1) {
                    outputString.append(counter + ", ");
                }
                else {
                    outputString.append(i + ", ");
                }

                outputString.append("\n");
            }

            // do the actual writing to file
            Log.i("WRITING", "Writing the data to file");
            bw.write(outputString.toString());
            // Log.i("CLOSING", "Closing the file that was written at " + Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).toString() + " /" + filename);
            bw.close();

            // scan the new media so the device knows it's there and it can be accessed
            MediaScannerConnection.scanFile(this,
                    new String[]{file.toString()}, null,
                    new MediaScannerConnection.OnScanCompletedListener() {
                        public void onScanCompleted(String path, Uri uri) {
                            Log.i("ExternalStorage", "Scanned " + path + ":");
                            Log.i("ExternalStorage", "-> uri=" + uri);
                        }
                    });

        } catch (IOException ex){
            Log.e("IOEXCEPTION", "There was a problem either finding the file, or writing");
            Log.e("ERRORMSG",ex.getMessage());
            Log.e("ERRORSTK",ex.getStackTrace().toString());
        }
    }

    private void updateFromPrefs()
    {
        int gridLength =
                mPreferences.getInt("grid_length", Defaults.GRID_LENGTH);
        int patternMin =
                mPreferences.getInt("pattern_min", Defaults.PATTERN_MIN);
        int patternMax =
                mPreferences.getInt("pattern_max", Defaults.PATTERN_MAX);
        String highlightMode =
                mPreferences.getString("highlight_mode", Defaults.HIGHLIGHT_MODE);
        boolean tactileFeedback = mPreferences.getBoolean("tactile_feedback",
                Defaults.TACTILE_FEEDBACK);

        // sanity checking
        if(gridLength < 1)
        {
            gridLength = 1;
        }
        if(patternMin < 1)
        {
            patternMin = 1;
        }
        if(patternMax < 1)
        {
            patternMax = 1;
        }
        int nodeCount = (int) Math.pow(gridLength, 2);
        if(patternMin > nodeCount)
        {
            patternMin = nodeCount;
        }
        if(patternMax > nodeCount)
        {
            patternMax = nodeCount;
        }
        if(patternMin > patternMax)
        {
            patternMin = patternMax;
        }

        // only update values that differ
        if(gridLength != mGridLength)
        {
            setGridLength(gridLength);
        }
        if(patternMax != mPatternMax)
        {
            setPatternMax(patternMax);
        }
        if(patternMin != mPatternMin)
        {
            setPatternMin(patternMin);
        }
        if(!highlightMode.equals(mHighlightMode))
        {
            setHighlightMode(highlightMode);
        }
        if(tactileFeedback ^ mTactileFeedback)
        {
            setTactileFeedback(tactileFeedback);
        }
    }

    private void setGridLength(int length)
    {
        mGridLength = length;
        mGenerator.setGridLength(length);
        mPatternView.setGridLength(length);
    }
    private void setPatternMin(int nodes) {
        mPatternMin = nodes;
        mGenerator.setMinNodes(nodes);
    }
    private void setPatternMax(int nodes) {
        mPatternMax = nodes;
        mGenerator.setMaxNodes(nodes);
    }
    private void setHighlightMode(String mode)
    {
        if("no".equals(mode))
        {
            mPatternView.setHighlightMode(new LockPatternView.NoHighlight());
        }
        else if("first".equals(mode))
        {
            mPatternView.setHighlightMode(new LockPatternView.FirstHighlight());
        }
        else if("rainbow".equals(mode))
        {
            mPatternView.setHighlightMode(
                    new LockPatternView.RainbowHighlight());
        }

        mHighlightMode = mode;
    }
    private void setTactileFeedback(boolean enabled)
    {
        mTactileFeedback = enabled;
        mPatternView.setTactileFeedbackEnabled(enabled);
    }

    private void recordTouchEventsNoVelocity(MotionEvent event){
        float xPos = event.getX();
        float yPos = event.getY();
        float pressure = event.getPressure();
        float size = event.getSize();
        xVelocityBuffer.add(0f);
        yVelocityBuffer.add(0f);
        xPositionsBuffer.add(xPos);
        yPositionsBuffer.add(yPos);
        pressureBuffer.add(pressure);
        sizeBuffer.add(size);
        counterBuffer.add(counter);
    }

    private void recordTouchEventsWithVelocity(MotionEvent event){
        float xPos = event.getX();
        float yPos = event.getY();
        float pressure = event.getPressure();
        float size = event.getSize();
        xVelocityBuffer.add(mVelocityTracker.getXVelocity());
        yVelocityBuffer.add(mVelocityTracker.getYVelocity());
        xPositionsBuffer.add(xPos);
        yPositionsBuffer.add(yPos);
        pressureBuffer.add(pressure);
        sizeBuffer.add(size);
        counterBuffer.add(counter);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (mPatternView.getPracticeMode()) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    if (mVelocityTracker == null){
                        mVelocityTracker = VelocityTracker.obtain();
                    } else {
                        mVelocityTracker.clear();
                    }
                    mRecordingSensorData = true;
                    recordTouchEventsNoVelocity(event);
                    break;
                case MotionEvent.ACTION_MOVE:
                    mVelocityTracker.addMovement(event);
                    mVelocityTracker.computeCurrentVelocity(1000);
                    recordTouchEventsWithVelocity(event);
                    break;
                case MotionEvent.ACTION_UP:
                    mRecordingSensorData = false;
                    recordTouchEventsNoVelocity(event);
                    counter++;
                    //processPracticeResults();
                    processPracticeResults_MeanAndDev();
                    Log.i("UP", "Touch lifted at (" + event.getX() + ", " + event.getY() + "), size:" + event.getSize() + ", pressure:" + event.getPressure());
                    break;
            }
        }
        return true;
    }

    public void successfulAttempt(){
        successes++;
        mSuccessCounterText.setText("Correct Attempts: " + successes);
    }

    /* Manipulates sensor data based on if the most recent practice pattern was correct */
    private void processPracticeResults() {
        if (mPatternView.testResult == "true") {
            Log.i("RESULT-PASS", "Practice pattern was correct, saving cache data to be written");
            // Save touch sensor cache
            xPositionsList.addAll(xPositionsBuffer);
            yPositionsList.addAll(yPositionsBuffer);

            xVelocityList.addAll(xVelocityBuffer);
            yVelocityList.addAll(yVelocityBuffer);

            pressureList.addAll(pressureBuffer);
            sizeList.addAll(sizeBuffer);

            // Save other sensor cache
            timeStampList.addAll(timeStampBuffer);

            xAccelerometerList.addAll(xAccelerometerBuffer);
            yAccelerometerList.addAll(yAccelerometerBuffer);
            zAccelerometerList.addAll(zAccelerometerBuffer);

            xMagneticList.addAll(xMagneticBuffer);
            yMagneticList.addAll(yMagneticBuffer);
            zMagneticList.addAll(zMagneticBuffer);

            xGyroscopeList.addAll(xGyroscopeBuffer);
            yGyroscopeList.addAll(yGyroscopeBuffer);
            zGyroscopeList.addAll(zGyroscopeBuffer);

            xRotationList.addAll(xRotationBuffer);
            yRotationList.addAll(yRotationBuffer);
            zRotationList.addAll(zRotationBuffer);

            xLinearAccelList.addAll(xLinearAccelBuffer);
            yLinearAccelList.addAll(yLinearAccelBuffer);
            zLinearAccelList.addAll(zLinearAccelBuffer);

            xGravityList.addAll(xGravityBuffer);
            yGravityList.addAll(yGravityBuffer);
            zGravityList.addAll(zGravityBuffer);

            counterList.addAll(counterBuffer);
        }
        else {  // Practice pattern incorrect
            Log.i("RESULT-FAIL", "Practice pattern was INCORRECT, emptying cached data");
            // Clear touch sensor data cache
            xPositionsBuffer.clear();
            yPositionsBuffer.clear();

            xVelocityBuffer.clear();
            yVelocityBuffer.clear();

            pressureBuffer.clear();
            sizeBuffer.clear();

            // Save other sensor cache
            timeStampBuffer.clear();

            xAccelerometerBuffer.clear();
            yAccelerometerBuffer.clear();
            zAccelerometerBuffer.clear();

            xMagneticBuffer.clear();
            yMagneticBuffer.clear();
            zMagneticBuffer.clear();

            xGyroscopeBuffer.clear();
            yGyroscopeBuffer.clear();
            zGyroscopeBuffer.clear();

            xRotationBuffer.clear();
            yRotationBuffer.clear();
            zRotationBuffer.clear();

            xLinearAccelBuffer.clear();
            yLinearAccelBuffer.clear();
            zLinearAccelBuffer.clear();

            xGravityBuffer.clear();
            yGravityBuffer.clear();
            zGravityBuffer.clear();

            counterBuffer.clear();
        }
    }

    private void processPracticeResults_MeanAndDev() {
        if (mPatternView.testResult == "true") {
            //Assignment two lists for Mean and Deviation
            xAccelerometer_MEAN.add((float) calculateMeanOfList(xAccelerometerBuffer));
            yAccelerometer_MEAN.add((float) calculateMeanOfList(yAccelerometerBuffer));
            zAccelerometer_MEAN.add((float) calculateMeanOfList(zAccelerometerBuffer));
            xMagnetic_MEAN.add((float) calculateMeanOfList(xMagneticBuffer));
            yMagnetic_MEAN.add((float) calculateMeanOfList(yMagneticBuffer));
            zMagnetic_MEAN.add((float) calculateMeanOfList(zMagneticBuffer));
            xGyroscope_MEAN.add((float) calculateMeanOfList(xGyroscopeBuffer));
            yGyroscope_MEAN.add((float) calculateMeanOfList(yGyroscopeBuffer));
            zGyroscope_MEAN.add((float) calculateMeanOfList(zGyroscopeBuffer));
            xRotation_MEAN.add((float) calculateMeanOfList(xRotationBuffer));
            yRotation_MEAN.add((float) calculateMeanOfList(yRotationBuffer));
            zRotation_MEAN.add((float) calculateMeanOfList(zRotationBuffer));
            xLinAccel_MEAN.add((float) calculateMeanOfList(xLinearAccelBuffer));
            yLinAccel_MEAN.add((float) calculateMeanOfList(yLinearAccelBuffer));
            zLinAccel_MEAN.add((float) calculateMeanOfList(zLinearAccelBuffer));
            xGrav_MEAN.add((float) calculateMeanOfList(xGravityBuffer));
            yGrav_MEAN.add((float) calculateMeanOfList(yGravityBuffer));
            zGrav_MEAN.add((float) calculateMeanOfList(zGravityBuffer));
            pressure_MEAN.add((float) calculateMeanOfList(pressureBuffer));
            size_MEAN.add((float) calculateMeanOfList(sizeBuffer));
            xVelocity_MEAN.add((float) calculateMeanOfList(xVelocityBuffer));
            yVelocity_MEAN.add((float) calculateMeanOfList(yVelocityBuffer));
            //////////////////////////////////
            xAccelerometer_STDEV.add((float) CalculateStandardDeviationOfList(xAccelerometerBuffer));
            yAccelerometer_STDEV.add((float) CalculateStandardDeviationOfList(yAccelerometerBuffer));
            zAccelerometer_STDEV.add((float) CalculateStandardDeviationOfList(zAccelerometerBuffer));
            xMagnetic_STDEV.add((float) CalculateStandardDeviationOfList(xGravityBuffer));
            yMagnetic_STDEV.add((float) CalculateStandardDeviationOfList(yGravityBuffer));
            zMagnetic_STDEV.add((float) CalculateStandardDeviationOfList(zGravityBuffer));
            xGyroscope_STDEV.add((float) CalculateStandardDeviationOfList(xGyroscopeBuffer));
            yGyroscope_STDEV.add((float) CalculateStandardDeviationOfList(yGyroscopeBuffer));
            zGyroscope_STDEV.add((float) CalculateStandardDeviationOfList(zGyroscopeBuffer));
            xRotation_STDEV.add((float) CalculateStandardDeviationOfList(xRotationBuffer));
            yRotation_STDEV.add((float) CalculateStandardDeviationOfList(yRotationBuffer));
            zRotation_STDEV.add((float) CalculateStandardDeviationOfList(zRotationBuffer));
            xLinAccel_STDEV.add((float) CalculateStandardDeviationOfList(xLinearAccelBuffer));
            yLinAccel_STDEV.add((float) CalculateStandardDeviationOfList(yLinearAccelBuffer));
            zLinAccel_STDEV.add((float) CalculateStandardDeviationOfList(zLinearAccelBuffer));
            xGrav_STDEV.add((float) CalculateStandardDeviationOfList(xGravityBuffer));
            yGrav_STDEV.add((float) CalculateStandardDeviationOfList(yGravityBuffer));
            zGrav_STDEV.add((float) CalculateStandardDeviationOfList(zGravityBuffer));
            pressure_STDEV.add((float) CalculateStandardDeviationOfList(pressureBuffer));
            size_STDEV.add((float) CalculateStandardDeviationOfList(sizeBuffer));
            xVelocity_STDEV.add((float) CalculateStandardDeviationOfList(xVelocityBuffer));
            yVelocity_STDEV.add((float) CalculateStandardDeviationOfList(yVelocityBuffer));
        }
        else {
            Log.i("RESULT-FAIL", "Practice pattern was INCORRECT, emptying cached data");
            // Clear touch sensor data cache
            xPositionsBuffer.clear();
            yPositionsBuffer.clear();

            xVelocityBuffer.clear();
            yVelocityBuffer.clear();

            pressureBuffer.clear();
            sizeBuffer.clear();

            // Save other sensor cache
            timeStampBuffer.clear();

            xAccelerometerBuffer.clear();
            yAccelerometerBuffer.clear();
            zAccelerometerBuffer.clear();

            xMagneticBuffer.clear();
            yMagneticBuffer.clear();
            zMagneticBuffer.clear();

            xGyroscopeBuffer.clear();
            yGyroscopeBuffer.clear();
            zGyroscopeBuffer.clear();

            xRotationBuffer.clear();
            yRotationBuffer.clear();
            zRotationBuffer.clear();

            xLinearAccelBuffer.clear();
            yLinearAccelBuffer.clear();
            zLinearAccelBuffer.clear();

            xGravityBuffer.clear();
            yGravityBuffer.clear();
            zGravityBuffer.clear();

            counterBuffer.clear();

        }
    }

    // this is a super dirty list, no validation
    static private float calculateMeanOfList(List<Float> list) {

        // Calculate the summation of the elements in the list
        float sum = 0;
        int n = list.size();

        for (int i = 0; i < list.size(); i++) {
            sum = sum + list.get(i);
        }

        return (sum / n);
    }

    static private double CalculateStandardDeviationOfList(List<Float> list) {

        double sum = 0;
        float mean = calculateMeanOfList(list);

        for (Float i : list) {
            sum +=  Math.pow((i - mean), 2);
        }
        return Math.sqrt( sum / ( list.size() - 1 ) ); // sample
    }
}