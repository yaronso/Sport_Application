import sys
import os
import pandas as pd

input_csv_path = r'.\src\DataBase\cities under 1000.csv'
output_csv_path1 = r'.\src\DataBase\countries_data.csv'
output_csv_path2 = r'.\src\DataBase\removed.csv'


def addition():
    # create country_id-country_name csv

    df = pd.read_csv(input_csv_path)
    new_df = df[['Country name EN', 'Country Code']].fillna(" ").copy()
    new_df.drop_duplicates(subset="Country Code", inplace=True)
    new_df.to_csv(output_csv_path1, index=False)

    # create city_name-country_id-country_name csv
    df = pd.read_csv(input_csv_path)
    new_df = df[['ASCII Name', 'Country Code', 'Country name EN']].fillna(" ").copy()
    new_df['ASCII Name'] = new_df['ASCII Name'].astype(str).str.lower()
    new_df['Country Code'] = new_df['Country Code'].astype(str).str.lower()
    new_df['Country name EN'] = new_df['Country name EN'].astype(str).str.lower()
    new_df.drop_duplicates(subset=None, inplace=True)
    new_df['Country Code'] = new_df['Country Code'].astype(str).str.upper()
    new_df.to_csv(output_csv_path2, index=False)


addition()
