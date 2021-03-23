package com.gamecodeschool.myfitnessapp;

import android.content.Intent;
import androidx.core.view.GravityCompat;

import android.os.Bundle;
import android.view.MenuItem;

import com.gamecodeschool.myfitnessapp.databases.CompletedExercises;
import com.google.android.material.navigation.NavigationView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;


//import android.support.design.widget.NavigationView;
//import androidx.core.widget.DrawerLayout;
//import android.support.v7.app.AppCompatActivity;



public class BaseNavDrawer extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    public void onBackPressed() {
        //if navigation drawer is open and back button is pressed, close drawer
        //else execute usual method
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        //compare input to each activity
        //if it matches, change to that activity
        if (id == R.id.nav_home) {
            Intent intent = new Intent(this, HomeActivity.class);
            startActivity(intent);
            overridePendingTransition(0, 0);
        } else if (id == R.id.nav_newWorkout) {
            CompletedExercises[] ex = new CompletedExercises[0];
            Intent intent = new Intent(this, NewWorkoutActivity.class);
            Bundle extras = new Bundle();
            extras.putSerializable("CompletedExercises",ex);
            intent.putExtras(extras);
            startActivity(intent);
            overridePendingTransition(0, 0);
        } else if (id == R.id.nav_pastWorkouts) {
            Intent intent = new Intent(this, ViewWorkoutsActivity.class);
            startActivity(intent);
            overridePendingTransition(0, 0);
        } else if (id == R.id.nav_seeProgress) {
            Intent intent = new Intent(this, SeeProgressActivity.class);
            startActivity(intent);
            overridePendingTransition(0, 0);
        } else if (id == R.id.nav_editGoals) {
            Intent intent = new Intent(this, EditGoalsActivity.class);
            startActivity(intent);
            overridePendingTransition(0, 0);
        } else if (id == R.id.nav_personalDetails) {
            Intent intent = new Intent(this, PersonalDetailsActivity.class);
            startActivity(intent);
            overridePendingTransition(0, 0);
        }

        //close navigation drawer
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
