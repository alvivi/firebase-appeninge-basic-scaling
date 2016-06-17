package me.alvivi.fabstest;

import com.firebase.client.*;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Logger;

public class MainServlet extends HttpServlet {
    private static final Logger log = Logger.getLogger(MainServlet.class.getName());

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        final Object lock = new Object();
        final PrintWriter out = resp.getWriter();
        final Firebase rootRef = new Firebase("https://alvivi-test.firebaseio.com");
        Firebase.goOnline();
        log.info("Firebase is ONLINE");
        rootRef.child("message").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Object value = dataSnapshot.getValue();
                out.printf("Value: %s\n", value.toString());
                log.info("Firebase is OFFLINE");
                synchronized (lock) {
                    lock.notify();
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                out.printf("Cancelled: %s\n", firebaseError.getMessage());
                Firebase.goOffline();
                log.info("Firebase is OFFLINE");
                synchronized (lock) {
                    lock.notify();
                }
            }
        });

        synchronized (lock) {
            try {
                lock.wait();
            } catch (Exception exception) {
                out.printf("Exception (wait): %s\n", exception.getMessage());
            }
        }
    }
}
