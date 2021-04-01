package main.java.utils.commands;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.util.concurrent.Future;

public class CommandHandler implements Runnable {
    private AsynchronousSocketChannel socketChannel;
    private boolean shouldExit;

    public CommandHandler(AsynchronousSocketChannel socketChannel) {
        this.socketChannel = socketChannel;
    }

    @Override
    public void run() {
        ByteBuffer buffer = ByteBuffer.allocate(256);

        if (socketChannel == null) {
            shouldExit = true;
        }

        while (!shouldExit) {
            try {
                buffer.clear();
                Future<Integer> rawResult = socketChannel.read(buffer);
                rawResult.get();

                buffer.flip();
                String result = new String(buffer.array()).trim();

                //TODO: Add the result to a list and fire event based on content.
                System.out.println(result);

                //TODO: Zero memory of buffer -> find a fix this is ugly.
                for (int i = 0;  i < buffer.limit(); i++)
                    buffer.put(i, (byte)0);
            }
            catch (Exception e) {
                System.out.println(e);
            }
        }
    }

    public void halt() {
        shouldExit = true;
    }
}
