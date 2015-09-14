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
    private List<Point> mEasterEggPattern;
    protected SharedPreferences mPreferences;
    protected int mGridLength=0;
    protected int mPatternMin=0;
    protected int mPatternMax=0;
    protected String mHighlightMode;
    protected boolean mTactileFeedback;
    protected boolean mRecordingTouchData;
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
    private String myStr = "";

    // these are lists used to save the touch information
    // ------- Part 4
    private List<Long> timeStampBuffer = new ArrayList<>();
    private List<Float> xAccelerometerBuffer, yAccelerometerBuffer, zAccelerometerBuffer = new ArrayList<>();
    private List<Float> xMagneticBuffer, yMagneticBuffer, zMagneticBuffer = new ArrayList<>();
    private List<Float> xGyroscopeBuffer, yGyroscopeBuffer, zGyroscopeBuffer = new ArrayList<>();
    private List<Float> xRotationBuffer, yRotationBuffer, zRotationBuffer = new ArrayList<>();
    private List<Float> xLinearAccelBuffer, yLinearAccelBuffer, zLinearAccelBuffer = new ArrayList<>();
    private List<Float> xGravityBuffer, yGravityBuffer, zGravityBuffer = new ArrayList<>();
    private List<Float> xPositionsBuffer, yPositionsBuffer = new ArrayList<>();
    private List<Float> xVelocityBuffer, yVelocityBuffer = new ArrayList<>();
    private List<Float> pressureBuffer, sizeBuffer = new ArrayList<>();

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        mGenerator = new PatternGenerator();

        setContentView(R.layout.activity_alp);
        mPatternView = (LockPatternView) findViewById(R.id.pattern_view);
        // button hookup and action
        mGenerateButton = (Button) findViewById(R.id.generate_button);
        mGenerateButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Log.i("click","'Randomly Generate Pattern' button was pressed.");
                        mPatternView.setPattern(mGenerator.getPattern());
                        mPatternView.invalidate();
                    }
                }
        );

        mPracticeToggle = (ToggleButton) findViewById(R.id.practice_toggle);
        mPracticeToggle.setOnCheckedChangeListener(
                new ToggleButton.OnCheckedChangeListener() {
                    public void onCheckedChanged(CompoundButton buttonView,
                                                 boolean isChecked) {

                        mGenerateButton.setEnabled(!isChecked);
                        if (!isChecked){
                            Log.i("SAVEDATA","Toggled back to false, save the data in the lists");
                            mRecordingTouchData = false;
                            mPatternView.setPracticeMode(false);
                            writeDataToCSV();
                            //TODO:clear data
                        }
                        else{
                            Log.i("RECORDINGDATA","Toggled to true, recording the data");
                            mRecordingTouchData = true;
                            mPatternView.setPracticeMode(true);
                        }
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
        mSensorManager.registerListener(this, mAccelerometer, mSensorManager.SENSOR_DELAY_NORMAL);
        mSensorManager.registerListener(this, mMagnetometer, mSensorManager.SENSOR_DELAY_NORMAL);
        mSensorManager.registerListener(this, mGyroscope, mSensorManager.SENSOR_DELAY_NORMAL);
        mSensorManager.registerListener(this, mRotation, mSensorManager.SENSOR_DELAY_NORMAL);
        mSensorManager.registerListener(this, mLinearAcc, mSensorManager.SENSOR_DELAY_NORMAL);
        mSensorManager.registerListener(this, mGravity, mSensorManager.SENSOR_DELAY_NORMAL);

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
            timeStampList.add(event.timestamp);
            if (event.sensor == mAccelerometer){
                xAccelerometerList.add(event.values[0]);
                yAccelerometerList.add(event.values[1]);
                zAccelerometerList.add(event.values[2]);
                Log.i("SENSORDATA","Accelerometer(" +
                        event.values[0] + ", " + event.values[1] + ", " + event.values[2] +")");
            }
            if (event.sensor == mMagnetometer){
                xMagneticList.add(event.values[0]);
                yMagneticList.add(event.values[1]);
                zMagneticList.add(event.values[2]);
                Log.i("SENSORDATA", "Magnetometer(" +
                        event.values[0] + ", " + event.values[1] + ", " + event.values[2] + ")");
            }
            if (event.sensor == mGyroscope){
                yGyroscopeList.add(event.values[0]);
                xGyroscopeList.add(event.values[1]);
                zGyroscopeList.add(event.values[2]);
                Log.i("SENSORDATA", "Gyroscope(" +
                        event.values[0] + ", " + event.values[1] + ", " + event.values[2] + ")");
            }
            if (event.sensor == mRotation){
                yRotationList.add(event.values[0]);
                zRotationList.add(event.values[1]);
                xRotationList.add(event.values[2]);
                Log.i("SENSORDATA", "Rotation(" +
                        event.values[0] + ", " + event.values[1] + ", " + event.values[2] + ")");
            }
            if (event.sensor == mLinearAcc){
                xLinearAccelList.add(event.values[0]);
                yLinearAccelList.add(event.values[1]);
                zLinearAccelList.add(event.values[2]);
                Log.i("SENSORDATA", "LinearAcceleration(" +
                        event.values[0] + ", " + event.values[1] + ", " + event.values[2] + ")");
            }
            if (event.sensor == mGravity){
                xGravityList.add(event.values[0]);
                yGravityList.add(event.values[1]);
                zGravityList.add(event.values[2]);
                Log.i("SENSORDATA", "Gravity(" +
                        event.values[0] + ", " + event.values[1] + ", " + event.values[2] + ")");
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
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
            String filename = "assignment1datadump.csv";
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
                    "position_X, position_Y, velocity_X, velocity_Y, pressure, size, \n");

            for(int i = 0; i < xPositionsList.size(); i++){
                if (i > timeStampList.size() - 1) {
                    outputString.append("bad, ");
                }else {
                    outputString.append(timeStampList.get(i) + ", ");
                }
                // this is the sensors
                if (i > xAccelerometerList.size() - 1) {
                    outputString.append("bad, ");
                } else {
                    outputString.append(xAccelerometerList.get(i) + ", ");
                }
                if (i > yAccelerometerList.size() - 1) {
                    outputString.append("bad, ");
                } else {
                    outputString.append(yAccelerometerList.get(i) + ", ");
                }
                if (i > zAccelerometerList.size() - 1) {
                    outputString.append("bad, ");
                } else {
                    outputString.append(zAccelerometerList.get(i) + ", ");
                }
                if (i > xMagneticList.size() - 1) {
                    outputString.append("bad, ");
                } else {
                    outputString.append(xMagneticList.get(i) + ", ");
                }
                if (i > yMagneticList.size() - 1) {
                    outputString.append("bad, ");
                } else {
                    outputString.append(yMagneticList.get(i) + ", ");
                }
                if (i > zMagneticList.size() - 1) {
                    outputString.append("bad, ");
                } else {
                    outputString.append(zMagneticList.get(i) + ", ");
                }if (i > xGyroscopeList.size() - 1) {
                    outputString.append("bad, ");
                } else {
                    outputString.append(xGyroscopeList.get(i) + ", ");}
                if (i > yGyroscopeList.size() - 1) {
                    outputString.append("bad, ");
                } else {
                    outputString.append(yGyroscopeList.get(i) + ", ");
                }
                if (i > zGyroscopeList.size() - 1) {
                    outputString.append("bad, ");
                 } else {
                    outputString.append(zGyroscopeList.get(i) + ", ");
                }
                if (i > xRotationList.size() - 1) {
                    outputString.append("bad, ");
                } else {
                    outputString.append(xRotationList.get(i) + ", ");
                }
                if (i > yRotationList.size() - 1) {
                    outputString.append("bad, ");
                } else {
                    outputString.append(yRotationList.get(i) + ", ");
                }
                if (i > zRotationList.size() - 1) {
                    outputString.append("bad, ");
                } else {
                    outputString.append(zRotationList.get(i) + ", ");
                }
                if (i > xLinearAccelList.size() - 1) {
                    outputString.append("bad, ");
                } else {
                    outputString.append(xLinearAccelList.get(i) + ", ");
                }
                if (i > yLinearAccelList.size() - 1) {
                    outputString.append("bad, ");
                } else {
                    outputString.append(yLinearAccelList.get(i) + ", ");
                }
                if (i > zLinearAccelList.size() - 1) {
                    outputString.append("bad, ");
                } else {
                    outputString.append(zLinearAccelList.get(i) + ", ");
                }
                if (i > xGravityList.size() - 1) {
                    outputString.append("bad, ");
                } else {
                    outputString.append(xGravityList.get(i) + ", ");
                }
                if (i > yGravityList.size() - 1) {
                    outputString.append("bad, ");
                } else {
                    outputString.append(yGravityList.get(i) + ", ");
                }
                if (i > zGravityList.size() - 1) {
                    outputString.append("bad, ");
                } else {
                    outputString.append(zGravityList.get(i) + ", ");
                }
                // X Position
                if (i > xPositionsList.size()-1){
                    outputString.append("bad, ");
                } else {
                    outputString.append(xPositionsList.get(i) + ", ");
                }
                // Y Position
                if (i > yPositionsList.size()-1){
                    outputString.append("bad, ");
                } else {
                    outputString.append(yPositionsList.get(i) + ", ");
                }
                // X Velocity
                if (i > xVelocityList.size()-1){
                    outputString.append("bad, ");
                } else {
                    outputString.append(xVelocityList.get(i) + ", ");
                }
                // Y Velocity
                if (i > yVelocityList.size()-1){
                    outputString.append("bad, ");
                } else {
                    outputString.append(yVelocityList.get(i) + ", ");
                }
                // Pressure
                if (i > pressureList.size()-1){
                    outputString.append("bad, ");
                } else {
                    outputString.append(pressureList.get(i) + ", ");
                }
                // Size
                if (i > sizeList.size()-1) {
                    outputString.append("bad, ");
                } else {
                    outputString.append(sizeList.get(i) + ", ");
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
    private void setPatternMin(int nodes)
    {
        mPatternMin = nodes;
        mGenerator.setMinNodes(nodes);
    }
    private void setPatternMax(int nodes)
    {
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

    private void recordEvents(MotionEvent event){
        float xPos = event.getX();
        float yPos = event.getY();
        float pressure = event.getPressure();
        float size = event.getSize();
        xVelocityList.add(0f);
        yVelocityList.add(0f);
        xPositionsList.add(xPos);
        yPositionsList.add(yPos);
        pressureList.add(pressure);
        sizeList.add(size);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mPatternView.onTouchEvent(event);
        if (mRecordingTouchData) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    mRecordingSensorData = true;
                    recordEvents(event);
                    Log.i("DOWN", "Touch started at (" + event.getX() + ", " + event.getY() + "), size:" + event.getSize() + ", pressure:" + event.getPressure());
                    break;
                case MotionEvent.ACTION_MOVE:
                    recordEvents(event);
                    Log.i("MOVE", "Touch moved at (" + event.getX() + ", " + event.getY() + "), size:" + event.getSize() + ", pressure:" + event.getPressure());
                    break;
                case MotionEvent.ACTION_UP:
                    mRecordingSensorData = false;
                    recordEvents(event);
                    counter++;
                    processPracticeResults();
                    Log.i("UP", "Touch lifted at (" + event.getX() + ", " + event.getY() + "), size:" + event.getSize() + ", pressure:" + event.getPressure());
                    break;
            }
        }
        return true;
    }

    private void processPracticeResults() {
        if (mPatternView.testResult == "true") {
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
        }
        else {  // Practice pattern incorrect
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
        }
    }
/*
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
    */
}