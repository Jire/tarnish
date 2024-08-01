package com.osroyale;

import org.jire.swiftfup.client.FileRequest;

public final class OnDemandFetcher {

    private final Client client;

    public int mapAmount = 0;

    public void start(StreamLoader versionList) {
/*		if (Configuration.USE_UPDATE_SERVER) {
			FileChecksumsRequest fileChecksumsRequest = client.fileClient.requestChecksums();
			try {
				fileChecksumsRequest.get(30, TimeUnit.SECONDS);
			} catch (InterruptedException | ExecutionException | TimeoutException e) {
				e.printStackTrace();
				return;
			}
		}*/

        byte[] array = versionList.getFile("map_index");
        /*try {
            array = Files.readAllBytes(Path.of("map_index"));
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }*/
        Buffer stream = new Buffer(array);
        int size = stream.readUnsignedShort();

        regions = new int[size];
        mapFiles = new int[size];
        landscapes = new int[size];
        // shit retarded? u sure u packed that map_index yh lol can redo if u want

        for (int index = 0; index < size; index++) {
            regions[index] = stream.readUnsignedShort();
            mapFiles[index] = stream.readUnsignedShort();
            landscapes[index] = stream.readUnsignedShort();
            mapAmount++;
        }
    }

    public int getNodeCount() {
        return 0;
    }

    public int getVersionCount(int j) {
        return versions[j].length;
    }

    public boolean loadData(int indexID, int fileID) {
        return loadData(indexID, fileID, true);
    }

    public boolean loadData(int indexID, int fileID, boolean flush) {
        //System.out.println("REQUESTED " + indexID + ":" + fileID + " (flush=" + flush + ")");
        FileRequest request = client.fileClient.request(indexID + 1, fileID);
        if (flush && !request.isDone()) {
            client.fileClient.flush();
            //System.out.println("FLUSHED for " + indexID+":"+fileID+" / " + flush);
        }
        return true;
    }

    /**
     * Just a reversed argument version of @{@link #loadData(int, int)}
     */
    public void writeRequest(int fileID, int indexID) {
        loadData(indexID, fileID, true);
    }

    public int resolve(int type, int regionX, int regionY) {
        int regionId = (regionX << 8) | regionY;
        for (int area = 0; area < regions.length; area++) {
            if (regions[area] == regionId) {
                if (type == 0) {
                    return mapFiles[area] > 9999 ? -1 : mapFiles[area];
                } else {
                    return landscapes[area] > 9999 ? -1 : landscapes[area];
                }
            }
        }

        return -1;
    }

    public void loadData(int id) {
        loadData(0, id);
    }

    public boolean method564(int i) {
        for (int k = 0; k < regions.length; k++)
            if (landscapes[k] == i)
                return true;
        return false;
    }

    public OnDemandFetcher(final Client client) {
        this.client = client;
        versions = new int[4][];
    }

    private int[] landscapes;
    private int[] mapFiles;
    private final int[][] versions;
    private int[] regions;

}
