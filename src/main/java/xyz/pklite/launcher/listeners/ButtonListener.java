/*
 *  Copyright (c) 2019. PKLite  - All Rights Reserved
 *  Unauthorized modification, distribution, or possession of this source file, via any medium is strictly prohibited.
 *  Proprietary and confidential. Refer to PKLite License file for more information on full terms of this copyright and to determine what constitutes authorized use.
 *  Written by PKLite(ST0NEWALL, others) <stonewall@pklite.xyz>, 2019
 *
 *
 */

package xyz.pklite.launcher.listeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import xyz.pklite.launcher.Launcher;
import xyz.pklite.launcher.Settings;
import xyz.pklite.launcher.components.AppFrame;
import xyz.pklite.launcher.net.Download;
import xyz.pklite.launcher.net.Update;
import xyz.pklite.launcher.utils.Utils;

public class ButtonListener implements ActionListener
{

	private static Download download;

	@Override
	public void actionPerformed(ActionEvent e)
	{
		switch (e.getActionCommand())
		{
			case "_":
				Launcher.app.setState(JFrame.ICONIFIED);
				break;
			case "X":
				System.exit(0);
				break;
			case "play":
				Thread playThread = new Thread(() ->
				{
					AppFrame.playButton.setEnabled(false);
					AppFrame.pbar.setString("Checking for Client Updates...");

					byte status = Update.updateExists();
					if (status == 0)
					{
						AppFrame.pbar.setString("Now Launching " + Settings.PROJECT_NAME + "!");
						Utils.launchClient();
						return;
					}
					if (status == 1 || status == 3)
					{
						if (download == null)
						{
							download = new Download(Settings.DOWNLOAD_URL);
							download.download();
						}
						else
						{
							switch (download.getStatus())
							{
								case Download.COMPLETE:
									return;
								case Download.DOWNLOADING:
									download.pause();
									break;
								case Download.PAUSED:
									download.resume();
									break;
								case Download.ERROR:
									JOptionPane.showMessageDialog(Launcher.app,
										"Eggs are not supposed to be green.",
										"Inane error",
										JOptionPane.ERROR_MESSAGE);
							}
						}
					}
				});
				playThread.start();
				break;

			default:
				break;
		}
	}
}