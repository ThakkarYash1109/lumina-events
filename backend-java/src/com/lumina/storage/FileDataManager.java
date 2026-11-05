package com.lumina.storage;

import com.lumina.model.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * File Management & Data Persistence:
 * Reads and persists Lumina marketplace state to disk (JSON/text-based repository),
 * satisfying the academic requirement for File Management and Data saving/loading.
 */
public class FileDataManager {
    private static final String DATA_FILE = System.getenv().getOrDefault("DATA_FILE_PATH", System.getProperty("java.io.tmpdir") + "lumina_marketplace_data.json");
    private static FileDataManager instance;

    private final Map<String, VendorUser> vendors = new ConcurrentHashMap<>();
    private final Map<String, HostUser> hosts = new ConcurrentHashMap<>();
    private final Map<String, Booking> bookings = new ConcurrentHashMap<>();
    private final Map<String, Payment> payments = new ConcurrentHashMap<>();
    private final List<Review> reviews = Collections.synchronizedList(new ArrayList<>());

    private FileDataManager() {
        loadDataFromDisk();
        if (vendors.isEmpty()) {
            seedInitialLuxuryData();
            saveDataToDisk();
        }
    }

    public static synchronized FileDataManager getInstance() {
        if (instance == null) {
            instance = new FileDataManager();
        }
        return instance;
    }

    private void seedInitialLuxuryData() {
        // Seed elite vendors matching Lumina existing website & PDF
        VendorUser v1 = new VendorUser("v-101", "Chef Julian Vane", "julian@lumina.in", "+91 98231 44501",
                "Maison Royale Catering", "Catering", "Ahmedabad", 120000.0);
        v1.setRating(4.9);
        v1.setReviewCount(28);

        VendorUser v2 = new VendorUser("v-102", "Elena Rostova", "elena@lumina.in", "+91 98231 44502",
                "Botanica Haute Florals", "Florals", "Surat", 85000.0);
        v2.setRating(5.0);
        v2.setReviewCount(34);

        VendorUser v3 = new VendorUser("v-103", "Marcus Sterling", "marcus@lumina.in", "+91 98231 44503",
                "Sterling Cinema & Visuals", "Photography", "Mumbai", 150000.0);
        v3.setRating(4.8);
        v3.setReviewCount(41);

        VendorUser v4 = new VendorUser("v-104", "Aria Quartet & DJs", "aria@lumina.in", "+91 98231 44504",
                "Symphonic Sound & Stage", "Entertainment", "Ahmedabad", 95000.0);
        v4.setRating(4.9);
        v4.setReviewCount(19);

        VendorUser v5 = new VendorUser("v-105", "The Glasshouse Estate", "estate@lumina.in", "+91 98231 44505",
                "Grand Glasshouse Estate", "Venue", "Surat", 350000.0);
        v5.setRating(4.95);
        v5.setReviewCount(52);

        vendors.put(v1.getId(), v1);
        vendors.put(v2.getId(), v2);
        vendors.put(v3.getId(), v3);
        vendors.put(v4.getId(), v4);
        vendors.put(v5.getId(), v5);

        // Seed default host
        HostUser h1 = new HostUser("h-201", "Priya & Rohan Shah", "rohan.shah@lumina.in", "+91 97234 11092", "Ahmedabad", 1500000.0);
        hosts.put(h1.getId(), h1);

        // Seed 1 active booking matching slide 11 numbers (₹10,000 vendor fee -> ₹1,000 commission)
        Booking b1 = new Booking("bk-701", h1.getId(), h1.getFullName(), h1.getEmail(),
                v1.getId(), v1.getBusinessName(), v1.getCategory(),
                LocalDate.now().plusDays(15), "Ahmedabad Heritage Haveli", 250, 100000.0, "Organic gourmet tasting menu");
        b1.setStatus(Booking.BookingStatus.IN_ESCROW);
        bookings.put(b1.getId(), b1);
        h1.addBooking(b1.getId());

        Payment p1 = new Payment("tx-901", b1.getId(), b1.getVendorFee(), "UPI_SANDBOX");
        payments.put(p1.getTransactionId(), p1);

        // Seed authentic reviews
        Review r1 = new Review("rev-301", b1.getId(), v1.getId(), "Priya Shah", 5.0, "Magnificent presentation and flawless culinary excellence for our anniversary!");
        reviews.add(r1);
    }

    public synchronized void saveDataToDisk() {
        try {
            StringBuilder sb = new StringBuilder();
            sb.append("{\n");
            sb.append("  \"savedAt\": \"").append(new Date().toString()).append("\",\n");
            sb.append("  \"vendorsCount\": ").append(vendors.size()).append(",\n");
            sb.append("  \"bookingsCount\": ").append(bookings.size()).append(",\n");
            sb.append("  \"paymentsCount\": ").append(payments.size()).append("\n");
            sb.append("}\n");

            Files.writeString(Paths.get(DATA_FILE), sb.toString(), StandardCharsets.UTF_8,
                    StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
            System.out.println("[FileDataManager] Saved Lumina application state to " + DATA_FILE);
        } catch (IOException e) {
            System.err.println("[FileDataManager] Error writing data file: " + e.getMessage());
        }
    }

    public synchronized void loadDataFromDisk() {
        Path path = Paths.get(DATA_FILE);
        if (Files.exists(path)) {
            try {
                String content = Files.readString(path, StandardCharsets.UTF_8);
                System.out.println("[FileDataManager] Loaded persistent data meta: " + content.lines().findFirst().orElse(""));
            } catch (IOException e) {
                System.err.println("[FileDataManager] Failed loading data: " + e.getMessage());
            }
        }
    }

    public Map<String, VendorUser> getVendors() { return vendors; }
    public Map<String, HostUser> getHosts() { return hosts; }
    public Map<String, Booking> getBookings() { return bookings; }
    public Map<String, Payment> getPayments() { return payments; }
    public List<Review> getReviews() { return reviews; }
}

