package com.osroyale.util.tools.wiki.parser;

import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import org.apache.logging.log4j.LogManager;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Parses a wiki page.
 *
 * @author Michael | Chex
 */
public abstract class WikiTableParser {

    /** The path to the wiki dump json file location. */
    private static final Path WRITE_PATH = Paths.get("./data/wiki");

    /** The path to the wiki page address. */
    protected static final String WIKI_LINK = "http://oldschoolrunescape.wikia.com/wiki/";

    private static final org.apache.logging.log4j.Logger logger = LogManager.getLogger();

    /** The gson object. */
    protected static final Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();

    /** A thread pool that will run tasks. */
    private final ListeningExecutorService executorService;

    /** The {@link WikiTable} list to parse. */
    protected final LinkedList<WikiTable> tables;

    /** Constructs a new {@code WikiTableParser} object. */
    public WikiTableParser(LinkedList<WikiTable> tables) {
        this.tables = tables;
        ExecutorService delegateService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors(),
                new ThreadFactoryBuilder().setNameFormat("WikiTableParserThread").build());
        executorService = MoreExecutors.listeningDecorator(delegateService);
    }

    /** Begins parsing wiki tables for all the names provided. */
    public void begin() throws InterruptedException {
        int size = tables.size();
        logger.info("Parsing " + size + " names from " + WIKI_LINK);

        tables.forEach(table -> execute(new ParserTask(table)));

        executorService.shutdown();
        executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.DAYS);

        finish();
    }

    protected void execute(Runnable runnable) {
        executorService.execute(runnable);
    }

    protected abstract void finish();

    /**
     * Writes the parsed wiki dump to a json file in {@code ./data/wiki} named
     * as the {@code name} parameter.
     *
     * @param name the name of the json file to dump to
     */
    protected static void writeToJson(String name, JsonElement array) {
        try {
            String json = GSON.toJson(array);

            if (!Files.exists(WRITE_PATH)) {
                Files.createDirectory(WRITE_PATH);
            }

            Path path = WRITE_PATH.resolve(name += ".json");
            Files.write(path, json.getBytes());

            logger.info("Successfully wrote " + path);
        } catch (IOException e) {
            logger.warn("Could not save '" + name + "' to '" + WRITE_PATH + "'", e);
        }
    }

    /**
     * A task that parses a web page doccument using JSoup.
     *
     * @author Michael | Chex
     */
    private final class ParserTask implements Runnable {

        /** The data to parse. */
        private final WikiTable table;

        /**
         * Constructs a new {@link ParserTask} object for a {@link WikiTable}
         * object.
         *
         * @param table the table to parse
         */
        private ParserTask(WikiTable table) {
            this.table = table;
        }

        @Override
        public void run() {
            try {
                String link = table.getLink();
                Document document = Jsoup.connect(link).get();
                table.parseDocument(document);
            } catch (Exception e) {
                System.out.println("Could not parse table from wiki for '" + table.getLink() + "'");
//                logger.error("Could not parse table from wiki for '" + table.getLink() + "'", e);
            }
        }
    }

}
