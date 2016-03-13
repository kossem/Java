        

    import java.util.ArrayList;
    import java.util.Collections;
    import java.util.HashSet;
    import java.util.List;
    import java.util.Set;
    import java.io.BufferedInputStream;
    import java.io.File;
    import java.io.FileInputStream;
    import java.io.IOException;
    import java.io.InputStream;
    import java.nio.file.FileSystems;
    import java.nio.file.FileVisitResult;
    import java.nio.file.Files;
    import java.nio.file.Path;
    import java.nio.file.SimpleFileVisitor;
    import java.nio.file.attribute.BasicFileAttributes;
     
    public class Sync {
        private static Set<Path> copy, delete;
        private static String S, D;
        static class Check extends SimpleFileVisitor<Path> {
            public boolean Equal(Path first, Path second) throws IOException {
                File file_first = first.toFile(), file_second = second.toFile();
                int value1 = 0, value2;
                if(file_first.length() != file_second.length()) { return false; }
                try (InputStream input_first = new BufferedInputStream(new FileInputStream(file_first));
                     InputStream input_second = new BufferedInputStream(new FileInputStream(file_second))) {
                    while(value1 >= 0) {
                        value1 = input_first.read();
                        value2 = input_second.read();
                        if (value1 != value2) { return false; }
                    }
                    return true;
                }
            }
            public FileVisitResult visitFile(Path file, BasicFileAttributes attribute) throws IOException {
                if(copy.contains(file.subpath(1, file.getNameCount()))) {
                    if(Equal(FileSystems.getDefault().getPath(S + FileSystems.getDefault().getSeparator() + file.subpath(1, file.getNameCount())), file)) {copy.remove(file.subpath(1, file.getNameCount())); }
                    else { delete.add(file.subpath(1, file.getNameCount())); }
                }
                else { delete.add(file.subpath(1, file.getNameCount())); }
                return FileVisitResult.CONTINUE;
            }
        }
        static class Add extends SimpleFileVisitor<Path> {
            public FileVisitResult visitFile(Path file, BasicFileAttributes attribute) {
                copy.add(file.subpath(1, file.getNameCount()));
                return FileVisitResult.CONTINUE;
            }
        }
        public static void InitFiles(String S, String D) throws IOException {
            delete = new HashSet<>(); copy = new HashSet<>();
            Files.walkFileTree(FileSystems.getDefault().getPath(String.valueOf(S)), new Add());
            Files.walkFileTree(FileSystems.getDefault().getPath(String.valueOf(D)), new Check());
        }
        public static void main(String[] args) throws IOException {
            S = args[0]; D = args[1];
            InitFiles(S, D);
            List<Path> list_delete = new ArrayList<>(delete);
            List<Path> list_copy = new ArrayList<>(copy);
            Collections.sort(list_copy); Collections.sort(list_delete);
            if(list_copy.isEmpty() && list_delete.isEmpty()) {
                System.out.println("IDENTICAL");
                System.exit(1);
            }
            for(Path p1 : list_delete) { System.out.println("DELETE " + p1); }
            for(Path p2 : list_copy) { System.out.println("COPY " + p2); }
        }
    }

