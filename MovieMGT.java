package sampletransMGT;

import java.util.*;
import java.sql.*;
import java.text.SimpleDateFormat;

public class MovieMGT
{
	public static void main(String[] args)
	{
		try
		{
			Class.forName("oracle.jdbc.OracleDriver");
			Connection con=DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe","anshul","xyz");

//			System.out.println("Connected");

			Scanner s=new Scanner(System.in);

			while(true)
			{
				System.out.println("Movie Management Application");
				System.out.println("*****************************");
				//System.out.println("\t1.Check All Movies");
				System.out.println("\t1.Create a User");
				System.out.println("\t2.Login");
//				System.out.println("\t2.Book Seat");
//				System.out.println("\t3.Check Your Booking");
//				System.out.println("\t4.View Booking Transaction");
				System.out.println("\t3.Exit");
//

				System.out.println("Enter Your Choice: ");
				int choice=s.nextInt();

				switch(choice)
				{
					case 1:
					{

						PreparedStatement ps2=con.prepareStatement("insert into usermovie (user_id,u_name,u_pass,create_time) values(?,?,?,?)");
						//String currentTime=new SimpleDateFormat("HH:MM:SS").format(new java.util.date);
						String pcurrenttime=new SimpleDateFormat("HH:mm:ss").format(new java.util.Date());

						System.out.println("Enter user-id: ");
						int uuid=s.nextInt();
						s.nextLine();
						System.out.println("Enter user-name: ");
						String uname=s.nextLine();
//						System.out.print("\n");
						System.out.println("Set a Password: ");
						String upass=s.nextLine();

						ps2.setLong(1, uuid);
						ps2.setString(2, uname);
						ps2.setString(3, upass);
						ps2.setString(4, pcurrenttime);

						int a=ps2.executeUpdate();

						if(a>0)
						{
							System.out.println("User Created Successfully");
						}
						else
						{
							System.out.println("Something went Wrong, Please try again Letter...");
						}
					}
					break;
					case 2:
					{
						s.nextLine();
						System.out.println("Enter user-name: ");
						String uname=s.nextLine();
						System.out.println("Enter user-pass: ");
						String upass=s.nextLine();

						PreparedStatement ps3=con.prepareStatement("select *from usermovie where U_NAME=? and U_PASS=?");
						ps3.setString(1, uname);
						ps3.setString(2, upass);
						ResultSet rs3=ps3.executeQuery();
						if(rs3.next())
						{
							System.out.println("Login Successfull");
							System.out.println("\t1.Book Ticket");
							System.out.println("\t2.View Your Booking");
							System.out.println("\t3.View Transaction");
							System.out.println("\t4.Log Out");
							System.out.println("Enter your choice: ");
							int choice1=s.nextInt();
							s.nextLine();

							boolean subMenuRunning = true;
							boolean running;
							while(subMenuRunning)
							{
								switch(choice1)
								{
									case 1:
									{
										PreparedStatement ps1=con.prepareStatement("select *from movies1");
										ResultSet rs1=ps1.executeQuery();

//										System.out.println("Movie-id\tTitle\tGenre\tDuration");
//										while(rs1.next())
//										{
//											System.out.println(rs1.getInt(1)+"\t"+rs1.getString(2)+"\t"+rs1.getString(3)+"\t"+rs1.getString(4));
//										}

										System.out.printf("%-8s%-25s%-18s%-10s%n", "Mov-id", "Title", "Genre", "Duration");
										while(rs1.next())
										{
										    System.out.printf("%-8d%-25s%-18s%-10s%n", rs1.getInt(1), rs1.getString(2), rs1.getString(3), rs1.getString(4));
										}

										System.out.println("Select Which movie you want to see: ");
										System.out.println("Enter Movie-id: ");
										int mid=s.nextInt();
										s.nextLine();

										PreparedStatement ps4=con.prepareStatement("select *from movies1 where MOVIE_ID=?");
										ps4.setLong(1, mid);
										ResultSet rs4=ps4.executeQuery();
										if(rs4.next())
										{
											System.out.println("Select a Theatre From Below: ");

											PreparedStatement ps5=con.prepareStatement("select *from theatre");
											ResultSet rs5=ps5.executeQuery();

//											System.out.printf("%-8s%-25s%-18s%-10s%n", "Thetre-id", "Name", "Location");
											while(rs5.next())
											{
//												System.out.printf("%-8d%-25s%-18s%-10s%n", rs5.getInt(1), rs5.getString(2), rs5.getString(3));
												System.out.println(rs5.getInt(1)+"\t"+rs5.getString(2)+"\t"+rs5.getString(3));
											}
											System.out.println("Enter Theatre-id: ");
											int tid=s.nextInt();
											s.nextLine();

											PreparedStatement ps6=con.prepareStatement("select *from theatre where THEATER_ID=?");
											ps6.setLong(1, tid);
											ResultSet rs6=ps6.executeQuery();
											if(rs6.next())
											{
												System.out.println("Enter How many tickets you want: ");
												int tickcount=s.nextInt();
												s.nextLine();

												PreparedStatement ps7=con.prepareStatement("select PRICE from movies1 where MOVIE_ID=?");
												ps7.setLong(1,mid);
												ResultSet rs7=ps7.executeQuery();
												if(rs7.next())
												{
													int amt=rs7.getInt(1);

													int totalamt=tickcount*amt;

//													System.out.println("Your Total Price: Rs."+totalamt);
													PreparedStatement ps8=con.prepareStatement("insert into booking (booking_id,user_id,book_time,TICKETS) values(?,?,?,?)");
													System.out.println("Enter Booking-id: ");
													long bookid=s.nextLong();
													s.nextLine();
													String currentime=new SimpleDateFormat("HH:MM:SS").format(new java.util.Date());
													PreparedStatement ps9=con.prepareStatement("select USER_ID from usermovie where U_NAME=?");
													ps9.setString(1, uname);
													ResultSet rs9=ps9.executeQuery();
													if(rs9.next())
													{
														int userid=rs9.getInt(1);
														ps8.setLong(1,bookid);
														ps8.setInt(2,userid);
														ps8.setString(3, currentime);
														ps8.setInt(4,tickcount);

														int c=ps8.executeUpdate();
														int b=ps9.executeUpdate();

														if(b>0)
														{
//															System.out.println("Congratualtions..Your ticket booked successfully");
															PreparedStatement ps10=con.prepareStatement("insert into transactionmovie (tid,BOOKING_ID,amount,p_time) values(?,?,?,?)");
															System.out.println("Enter transaction-id: ");
															long transid=s.nextLong();
															String currenttime1=new SimpleDateFormat("HH:MM:SS").format(new java.util.Date());
															ps10.setLong(1, transid);
															ps10.setLong(2,bookid);
															ps10.setInt(3,totalamt);
															ps10.setString(4, currenttime1);

															int d=ps10.executeUpdate();
															if(d>0)
															{
																System.out.println("Your Ticket Booked Successfully");
																break;
															}
															else
															{
																System.out.println("Something went Wrong");
															}

														}
													}
												}
											}
										}
										else
										{
											System.out.println("No Movie Found");
										}
									}
									break;
									case 2:
									{
										System.out.println("Enter User-id: ");
										int useridd=s.nextInt();
										s.nextLine();
										PreparedStatement ps11=con.prepareStatement("select *from usermovie where USER_ID=?");
										ps11.setInt(1, useridd);
										ResultSet rs11=ps11.executeQuery();
										if(rs11.next())
										{
											PreparedStatement ps12=con.prepareStatement("select *from booking where USER_ID=?");
											ps12.setInt(1, useridd);
										}
										else
										{
											System.out.println("Invalid User");
										}
									}
									break;
									case 3:
									{
										System.out.println("Enter your Booking-id: ");
										long bid=s.nextLong();
										s.nextLine();
										PreparedStatement ps13=con.prepareStatement("select *from transactionmovie where BOOKING_ID=?");
										ps13.setLong(1,bid);
										ResultSet rs13=ps13.executeQuery();
										if(rs13.next())
										{

										}
										else
										{
											System.out.println("Invalid Booking-id");
										}
									}
									break;
									case 4:
									{
										System.out.println("Log Out Successfull");
										subMenuRunning = false; // Exits nested
			                            running = false;        // Exits main loop
			                            break;
									}
									default:
										System.out.println("Invalid Choice");
								}
							}

						}
						else
						{
							System.out.println("No user found");
						}
					}
					break;
					case 3:
					{
						System.exit(0);
					}
					break;
					default:
						System.out.println("Invalid Choice");
				}
			}
		}
		catch(Exception e)
		{
			System.out.println(e.getMessage());
		}
	}

}
