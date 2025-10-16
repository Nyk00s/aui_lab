package org.example;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.ForkJoinPool;

public class Main {
    public static void main(String[] args) throws Exception {
        System.out.println("Task 2:");
        List<Album> albums = createAlbums();
        albums.forEach(a -> {
            System.out.println(a);
            a.getSongs().forEach(s -> System.out.println(" " + s));
        });

        System.out.println("\nTask 3:");
        Set<Song> allSongs = albums.stream().flatMap(a -> a.getSongs().stream()).collect(Collectors.toSet());
        allSongs.forEach(System.out::println);

        System.out.println("\nTask 4:");
        allSongs.stream().filter(s -> s.getSeconds() >= 200)
                .sorted(Comparator.comparing(Song::getName, String.CASE_INSENSITIVE_ORDER))
                .forEach(System.out::println);

        System.out.println("\nTask 5:");
        List<SongDTO> songsdto = allSongs.stream()
                .map(s -> new SongDTO(s.getName(), s.getSeconds(), s.getAlbum() == null ? null : s.getAlbum().getName()))
                .sorted()
                .toList();
        songsdto.forEach(System.out::println);

        System.out.println("\nTask 6:");
        Path file = Files.createTempFile("albums-", ".bin");
        System.out.println("\nSerializing albums to: " + file);
        try (ObjectOutputStream oos = new ObjectOutputStream(Files.newOutputStream(file))) {
            oos.writeObject(albums);
        }

        System.out.println("Deserializing albums from: " + file);
        try (ObjectInputStream ois = new ObjectInputStream(Files.newInputStream(file))) {
            Object obj = ois.readObject();
            if (obj instanceof List) {
                List<?> read = (List<?>) obj;
                System.out.println("\n Deserialized albums");
                read.forEach(System.out::println);
            }
        }

        System.out.println("\nTask 7:");
        System.out.println("2");
        parallelTask(albums, 2);
        System.out.println("4");
        parallelTask(albums, 4);

    }

    private static List<Album> createAlbums() {
        Album hybrid_theory = new Album.Builder().name("Hybird Theory").yearOfRelease(2000).build();
        Album meteora = new Album.Builder().name("Meteora").yearOfRelease(2003).build();
        Album from_zero = new Album.Builder().name("From Zero").yearOfRelease(2024).build();
        Album nevermind = new Album.Builder().name("Nevermind").yearOfRelease(1991).build();
        Album in_utero = new Album.Builder().name("In Utero").yearOfRelease(1993).build();
        Album one_x = new Album.Builder().name("One-X").yearOfRelease(2006).build();

        Song in_the_end = new Song.Builder().name("In the End").seconds(216).album(hybrid_theory).build();
        Song crawling = new Song.Builder().name("Crawling").seconds(208).album(hybrid_theory).build();
        Song numb = new Song.Builder().name("Numb").seconds(187).album(meteora).build();
        Song faint = new Song.Builder().name("Faint").seconds(216).album(meteora).build();
        Song the_emptiness_machine = new Song.Builder().name("The Emptiness Machine").seconds(190).album(from_zero).build();
        Song two_faced = new Song.Builder().name("Two Faced").seconds(183).album(from_zero).build();
        Song smells_like_teen_spirit = new Song.Builder().name("Smells Like Teen Spirit").seconds(301).album(nevermind).build();
        Song come_as_you_are = new Song.Builder().name("Come as you are").seconds(218).album(nevermind).build();
        Song heart_shaped_box = new Song.Builder().name("Heart-Shaped Box").seconds(281).album(in_utero).build();
        Song all_apologies = new Song.Builder().name("All Apologies").seconds(233).album(in_utero).build();
        Song animal_i_have_become = new Song.Builder().name("Animal I Have Become").seconds(231).album(one_x).build();
        Song time_of_dying = new Song.Builder().name("Time Of Dying").seconds(186).album(one_x).build();

        hybrid_theory.addSong(new Song.Builder().name("Papercut").seconds(184).build());
        meteora.addSong(new Song.Builder().name("Breaking the Habit").seconds(196).build());
        from_zero.addSong(new Song.Builder().name("Heavy Is the Crown").seconds(167).build());
        nevermind.addSong(new Song.Builder().name("Lithium").seconds(257).build());
        in_utero.addSong(new Song.Builder().name("Rape Me").seconds(170).build());
        one_x.addSong(new Song.Builder().name("Riot").seconds(207).build());


        List<Album> list = new ArrayList<>();
        list.add(hybrid_theory);
        list.add(meteora);
        list.add(from_zero);
        list.add(nevermind);
        list.add(in_utero);
        list.add(one_x);
        return list;
    }

    private static void parallelTask(List<Album> albums, int p) throws Exception {
        ForkJoinPool pool = new ForkJoinPool(p);
        try {
            pool.submit( () ->
                    albums.parallelStream().forEach(a -> {
                        System.out.println(Thread.currentThread().getName() + " - Processing " + a.getName());
                        a.getSongs().forEach(s -> {
                            try {
                                Thread.sleep(100);
                            } catch (InterruptedException e) {
                                Thread.currentThread().interrupt();
                            }
                            System.out.println(Thread.currentThread().getName() + " -> " + s);
                        });
                    })
            ).get();
        } finally {
            pool.shutdown();
        }
    }
}